<script setup lang="ts">
import { ref, watch } from "vue";
import "../assets/tableStyle.css";

interface Party {
  name: string;
  votes: number;
}

const parties = ref<Party[]>([]);
const isVisible = ref(false);
const selectedYear = ref("2023"); // Standaard 2023
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;

// Fetch data als selectedYear verandert
watch(selectedYear, async (newYear) => {
  await fetchData(newYear);
}, { immediate: true });

async function fetchData(year: string) {
  try {
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/xml/votes/parties?year=${year}`);
    if (response.ok) {
      const data = await response.json();
      parties.value = Object.entries(data).map(([name, votes]) => ({
        name,
        votes: Number(votes),
      }));
      isVisible.value = true;
    } else {
      parties.value = [];
      isVisible.value = true;
    }
  } catch (error) {
    console.error("Fout bij ophalen data", error);
    parties.value = [];
    isVisible.value = true;
  }
}
</script>

<template>
  <div class="dropdown-wrapper">
    <select v-model="selectedYear" class="dropdown">
      <option disabled value="">Selecteer jaar</option>
      <option value="2023">2023</option>
      <option value="2021">2021</option>
    </select>
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon" aria-hidden="true">
      <path d="m6 9 6 6 6-6"></path>
    </svg>
  </div>

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

    <p v-if="isVisible && parties.length === 0">Geen partijen gevonden...</p>
  </div>
</template>
