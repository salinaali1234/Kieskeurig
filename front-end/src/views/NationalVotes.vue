<script setup lang="ts">
import { ref, onMounted } from "vue";
import "../assets/tableStyle.css"

interface Party {
  name: string;
  votes: number;
}

const parties = ref<Party[]>([]);
const isVisible = ref(false);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
const url: string = `${VITE_APP_BACKEND_URL}/api/xml/votes/parties`;

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

onMounted(async () => {
  try {
    const response = await fetch(url);

    if (response.ok) {
      const data = await response.json();
      parties.value = Object.entries(data).map(([name, votes]) => ({
        name,
          votes: Number(votes),
          }));
        }
      } catch (error) {
    console.error("Er is een fout opgetreden bij het ophalen van de data", error);
  }
});
</script>

<template>
  <div>

    <table class="data-table">
      <thead>
      <tr>
        <th>Partij</th>
        <th>Aantal Stemmen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(party, index) in parties" :key="index">
        <td>{{ party.name }}</td>
        <td>{{ party.votes }}</td>
      </tr>
      </tbody>
    </table>
    <p >Geen partijen gevonden...</p>
  </div>
</template>
