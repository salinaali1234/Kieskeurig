<script setup lang="ts">
import { ref, onMounted, type Ref, computed, watchEffect } from "vue";
import "../assets/tableStyle.css";
import { useRoute } from "vue-router";
import ProvinceDonutChart from "@/components/charts/donut-charts/ProvinceDonutChart.vue";

const route = useRoute();
const constituencyId = computed(() => route.params.constituencyId);
const constituencyName = computed(() => route.params.constituencyName);

const municipalities: Ref<any[]> = ref([]);
const results: Ref<any[]> = ref([]);
let labels: Ref<string[]> = ref([]);
let votes: Ref<number[]> = ref([]);

const selectedYear = ref("2023"); // ✅ default year
const selectedView = ref("national"); // default view
const sortDirection = ref<'asc' | 'desc' | null>(null);
const isVisible = ref(false);

const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const urlMunicipalities = `${backendUrl}/api/municipalities/all/${constituencyId.value}`;

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

onMounted(async () => {
  try {
    const response = await fetch(urlMunicipalities);
    if (response.ok) {
      const data = await response.json();
      for (const municipality of Object.entries(data)) {
        municipalities.value.push(municipality);
      }
    }
  } catch (error) {
    console.error(error);
  }

  await fetchVotes(buildVotesUrl());
});

watchEffect(async () => {
  if (!selectedYear.value) return;

  const urlVotes = buildVotesUrl();
  await fetchVotes(urlVotes);
});

function buildVotesUrl(): string {
  if (!selectedView.value || selectedView.value === "national") {
    return `${backendUrl}/api/constituencies/Info/${constituencyName.value}`;
  } else {
    console.log("het is een gemeents")
    return `${backendUrl}/api/municipalities/Info/${selectedView.value}/${selectedYear.value}`;
  }
}

async function fetchVotes(url: string) {
  try {
    const response = await fetch(url);
    if (response.ok) {
      const data = await response.json();
      results.value = [];
      for (const [party, count] of Object.entries(data)) {
        results.value.push([party, count]);
      }
      await populateProps();
    }
  } catch (error) {
    console.error("Fout bij ophalen van stemmen:", error);
  }
}

async function populateProps() {
  const labelsLocal: string[] = [];
  const votesLocal: number[] = [];

  for (const [party, voteCount] of results.value) {
    labelsLocal.push(party);
    votesLocal.push(voteCount);
  }

  labels.value = labelsLocal;
  votes.value = votesLocal;
}

const displayedVotes = computed(() => {
  if (sortDirection.value === 'asc') {
    return [...results.value].sort((a, b) => a[0].localeCompare(b[0]));
  }
  if (sortDirection.value === 'desc') {
    return [...results.value].sort((a, b) => b[0].localeCompare(a[0]));
  }
  return results.value;
});

const displayedMunicipalities = computed(() => {
  if (sortDirection.value === 'asc') {
    return [...municipalities.value].sort((a, b) => a[0].localeCompare(b[0]));
  }
  if (sortDirection.value === 'desc') {
    return [...municipalities.value].sort((a, b) => b[0].localeCompare(a[0]));
  }
  return municipalities.value;
});

function toggleSortByName() {
  if (sortDirection.value === null) {
    sortDirection.value = 'asc';
  } else if (sortDirection.value === 'asc') {
    sortDirection.value = 'desc';
  } else {
    sortDirection.value = null;
  }
}
</script>

<template>
  <div>
    <h1 class="page-title">Kieskring {{ constituencyName }}</h1>

    <!-- Year Dropdown -->
    <table class="data-table">
      <tbody>
      <tr class="dropdown-wrapper">
        <select v-model="selectedYear" class="dropdown">
          <option disabled value="">Selecteer jaar</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>
        <span class="dropdown-icon">˅</span>
      </tr>
      </tbody>
    </table>

    <!-- Municipality Dropdown -->
    <table class="data-table">
      <tbody>
      <tr class="dropdown-wrapper">
        <select v-model="selectedView" class="dropdown">
          <option value="national">Landelijk resultaat</option>
          <option
            v-for="municipality in displayedMunicipalities"
            :key="municipality[0]"
            :value="municipality[0]"
          >
            {{ municipality[0] }}
          </option>
        </select>
        <span class="dropdown-icon">˅</span>
      </tr>
      </tbody>
    </table>

    <!-- Sort Button -->
    <thead class="sortingButton">
    <tr>
      <th @click="toggleSortByName()" style="cursor: pointer;">
        <span v-if="sortDirection == null">Niet gesorteerd</span>
        <span v-if="sortDirection === 'asc'">A-Z ▼▲</span>
        <span v-else-if="sortDirection === 'desc'">Z-A ▲▼</span>
      </th>
    </tr>
    </thead>

    <!-- Donut Chart -->
    <ProvinceDonutChart :labels="labels" :votes="votes" :province="null" :year="selectedYear" />

    <!-- Votes Table -->
    <table class="data-table">
      <thead>
      <tr>
        <th>Partij</th>
        <th>Aantal Stemmen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="results in displayedVotes" :key="results[0]">
        <td>{{ results[0] }}</td>
        <td>{{ results[1] }}</td>
      </tr>
      </tbody>
    </table>

    <p v-if="isVisible && municipalities.length === 0">Alle gemeentes binnen deze kieskring inladen...</p>
  </div>
</template>

<style scoped>
.page-title {
  text-align: center;
  font-size: 2rem;
  margin-top: 40px;
  margin-bottom: 20px;
  font-weight: bold;
  color: white;
}

.dropdown-wrapper {
  position: relative;
  width: 80%;
  max-width: 600px;
  margin: 20px auto;
}

.dropdown {
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  border-radius: 8px;
  border: 1px solid var(--secondary-clr);
  background-color: #DEBFE9;
  color: black;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
}

.dropdown-icon {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2rem;
  color: black;
  pointer-events: none;
}
</style>
