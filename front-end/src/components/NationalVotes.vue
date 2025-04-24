<script setup lang="ts">
import {ref, onMounted, watch} from "vue";
import "../assets/tableStyle.css"

interface Party {
  name: string;
  votes: number;
}

const parties = ref<Party[]>([]);
const isVisible = ref(false);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
const props = defineProps<{ year: string }>();
const url: string = `${VITE_APP_BACKEND_URL}/api/xml/votes/parties?year=${props.year}`;



function toggleVisible() {
  isVisible.value = !isVisible.value;
}

watch(() => props.year, async (newYear) => {
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
    }
  } catch (error) {
    console.error("Fout bij ophalen data", error);
  }
}


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
    <p v-if="isVisible && parties.length === 0">Geen partijen gevonden...</p>
  </div>
</template>
