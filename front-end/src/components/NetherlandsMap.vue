<script setup lang="ts">
import "leaflet/dist/leaflet.css"
import L from "leaflet"
import { onMounted } from "vue"

const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;

const getProvinceColors = async () => {
  try {
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/provinces`);
    if (!response.ok) throw new Error("Fout bij ophalen provincies");

    const provinces: string[] = await response.json();

    const colors: Record<string, string> = {};
    provinces.forEach(province => {
      colors[province.toLowerCase()] = `hsl(${Math.floor(Math.random() * 360)}, 70%, 75%)`;
    });

    return colors;
  } catch (e) {
    console.error("Provincies ophalen mislukt:", e);
    return {};
  }
};

const normalizeName = (name: string) =>
  name.toLowerCase().replace(/\s+/g, '').replace(/-/g, '');

const bindProvincePopup = async (feature: any, layer: any, electionId = "TK2023") => {
  const rawName = feature.properties?.name || 'Onbekend';
  const name = rawName;

  try {
    const voteResponse = await fetch(
      `${VITE_APP_BACKEND_URL}/api/provinces/${encodeURIComponent(name)}/votes?electionId=${electionId}`
    );

    if (voteResponse.ok) {
      const voteData = await voteResponse.json();
      layer.bindPopup(
        `<b>${name}</b><br/>Totaal stemmen: ${voteData.totalVotes.toLocaleString("nl-NL")}`
      );
    } else {
      layer.bindPopup(`<b>${name}</b><br/>Stemdata niet beschikbaar`);
    }
  } catch (err) {
    console.error(`Fout bij stemmen ophalen voor ${name}:`, err);
    layer.bindPopup(`<b>${name}</b><br/>Fout bij laden`);
  }
};

onMounted(async () => {
  const provinceColors = await getProvinceColors();

  const map = L.map('map', {
    center: [52.1, 5.3],
    zoom: 7,
    minZoom: 7,
    maxZoom: 10,
    maxBounds: L.latLngBounds([50.7, 3.2], [53.6, 7.2]),
    maxBoundsViscosity: 1.0
  });

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(map);

  try {
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/map/map`);
    if (!response.ok) throw new Error(`HTTP fout: ${response.status}`);

    const geojson = await response.json();

    const geojsonLayer = L.geoJSON(geojson, {
      style: feature => {
        const rawName = feature.properties?.name || '';
        const normalizedName = normalizeName(rawName);
        const fillColor = Object.entries(provinceColors).find(
          ([key]) => normalizeName(key) === normalizedName
        )?.[1] || '#cccccc';

        return {
          color: '#000000',
          weight: 1,
          opacity: 1,
          fillColor,
          fillOpacity: 0.9
        };
      },
      onEachFeature: (feature, layer) => {
        bindProvincePopup(feature, layer); // async popup
      }
    }).addTo(map);

    map.fitBounds(geojsonLayer.getBounds());
  } catch (e) {
    console.error('Fout bij ophalen GeoJSON:', e);
  }
});
</script>


<template>
  <div class="intro-container">
    <h1 class="intro-title">
      Welkom bij
        Kies<span class="highlight">Keurig</span>!
    </h1>
    <p class="intro-text">
      Jouw plek voor een heldere overzicht van de verkiezingsdata van Nederland.
      Ontdek hoe partijen en kandidaten hebben gescoord in jouw regio!
    </p>
    <div id="map" style="height: 600px; width: 100%; margin-top: 20px;"></div>
  </div>
</template>


<style scoped>
.intro-container {
  width: 90%;
  margin: 2rem auto;
}

.intro-title {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  color: var(--text-clr);
}

.intro-text {
  font-size: 1.1rem;
  color: var(--text-clr);
  max-width: 800px;
  line-height: 1.6;
}

.highlight {
  color: var(--accent-clr);
}

</style>
