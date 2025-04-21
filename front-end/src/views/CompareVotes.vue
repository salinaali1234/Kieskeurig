<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import "../assets/tableStyle.css";

interface Party {
  name: string;
  votes: number;
}

const years = ["2021", "2023"];
const selectedYearA = ref("2023");
const selectedYearB = ref("2021");

const votesA = ref<Party[]>([]);
const votesB = ref<Party[]>([]);

const uniqueParties = ref<string[]>([]);

const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;

async function fetchVotes(year: string): Promise<Party[]> {
  const response = await fetch(`${VITE_APP_BACKEND_URL}/api/xml/votes/parties?year=${year}`);
  if (!response.ok) return [];
  const data = await response.json();
  return Object.entries(data).map(([name, votes]) => ({
    name,
    votes: Number(votes),
  }));
}

async function loadVotes() {
  votesA.value = await fetchVotes(selectedYearA.value);
  votesB.value = await fetchVotes(selectedYearB.value);

  const allPartyNames = new Set([
    ...votesA.value.map(p => p.name),
    ...votesB.value.map(p => p.name)
  ]);

  uniqueParties.value = Array.from(allPartyNames).sort();
}


onMounted(loadVotes);
watch([selectedYearA, selectedYearB], loadVotes);
</script>

<template>
  <div class="container">
    <h1 class="page-title">Vergelijk verkiezingsresultaten</h1>

    <div class="dropdowns">
      <div>
        <label>Jaar A</label>
        <select v-model="selectedYearA" class="dropdown">
          <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
        </select>
      </div>

      <div>
        <label>Jaar B</label>
        <select v-model="selectedYearB" class="dropdown">
          <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
        </select>
      </div>
    </div>

    <div class="compare-table-wrapper">
      <table class="data-table">
        <thead>
        <tr>
          <th>Partij</th>
          <th>{{ selectedYearA }}</th>
          <th>{{ selectedYearB }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="partyName in uniqueParties" :key="partyName">
          <td>{{ partyName }}</td>
          <td>{{ votesA.find(p => p.name === partyName)?.votes ?? '-' }}</td>
          <td>{{ votesB.find(p => p.name === partyName)?.votes ?? '-' }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.page-title {
  text-align: center;
  font-size: 2rem;
  margin: 40px 0;
  font-weight: bold;
  color: white;
}

.dropdowns {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

label {
  display: block;
  margin-bottom: 4px;
  color: white;
}

.dropdown {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid var(--secondary-clr);
  background-color: #DEBFE9;
  color: black;
  min-width: 120px;
}

.compare-table-wrapper {
  overflow-x: auto;
  padding: 0 1rem;
}
</style>
