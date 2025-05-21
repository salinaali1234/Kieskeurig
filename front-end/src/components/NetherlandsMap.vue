<script setup lang="ts">
import "leaflet/dist/leaflet.css"
import L from "leaflet"
import { onMounted } from "vue"

const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;

onMounted(async () => {
  const map = L.map('map', {
    minZoom: 6,
    maxZoom: 10,
    maxBounds: L.latLngBounds(
      [50.5, 3.1],
      [53.7, 7.3]
    ),
    maxBoundsViscosity: 1.0
  });

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(map);

  try {
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/map/map`);
    if (!response.ok) {
      throw new Error(`HTTP fout: ${response.status}`);
    }

    const geojson = await response.json();

    const geojsonLayer = L.geoJSON(geojson, {
      style: {
        color: '#3388ff',
        weight: 2,
        opacity: 1
      },
      onEachFeature: (feature, layer) => {
        const name = feature.properties?.name || 'Onbekend';
        layer.bindPopup(`<b>${name}</b>`);
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
<!--      Bekijk resultaten op nationaal, provinciaal, gemeentelijk niveau en per kieskring.-->
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
