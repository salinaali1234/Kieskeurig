ChartTest<script setup lang="ts">
import {ref, onMounted, type Ref} from "vue";
import "../assets/tableStyle.css"
import ProvinceDonutChart from "@/components/charts/donut-charts/ProvinceDonutChart.vue";

interface Party {
  partyName: string;
  validVotes: number;
}

let electionId = "TK2023" // Temp
let sort = "validVotes"
let asc = false

const selectedProvince = ref("");
const selectedYear = ref("");
const provinces = ref<string[]>([]);
const years = ref<string[]>([]);
const parties = ref<Party[]>([]);
const isVisible = ref(false);
const isVisible2 = ref(false);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
let provinceUrl: string = `${VITE_APP_BACKEND_URL}/api/provinces`;
let partyUrl: string = "";

let labels: Ref<string[]> = ref([])
let votes: Ref<number[]> = ref([])

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

function toggleVisible2() {
  isVisible2.value = !isVisible2.value;
}

onMounted(async () => {
  try {
    const response = await fetch(provinceUrl);

    if (response.ok) {
      provinces.value = await response.json();
    }
  } catch (error) {
    console.error("Er is een fout opgetreden bij het ophalen van de data", error);
  }
});

async function onClickDropdown() {
  isVisible.value = true;

  if (selectedYear.value === "") {
    await fetchYearData()
  } else {
    partyUrl = `${VITE_APP_BACKEND_URL}/api/party/${electionId}/${selectedProvince.value}?sort=${sort}&asc=${asc}`;

    await fetchPartyData();
    await populateProps();
  }
}

async function onClickDropdown2(year: string) {
  electionId = "TK" + year
  partyUrl = `${VITE_APP_BACKEND_URL}/api/party/${electionId}/${selectedProvince.value}?sort=${sort}&asc=${asc}`;
  isVisible2.value = true;

  await fetchPartyData();
  await populateProps();
}

async function onClickTable(event) {
  const targetElement: HTMLElement = event.target;
  const targetId: string = targetElement.id;
  const sortedColumnClassName: string = "sortedColumn";
  const svgElement: HTMLElement = document.querySelector(`#${targetElement.id} svg`)!

  if (targetElement.classList.contains(sortedColumnClassName)) {
    asc = !asc;
  } else {
    asc = false

    const previousElement: HTMLElement = document.querySelector(`.${sortedColumnClassName}`)!
    previousElement.classList.remove(sortedColumnClassName)
    targetElement.classList.add(sortedColumnClassName)

    const previousSVG: HTMLElement = document.querySelector(`#${previousElement.id} svg`)!
    previousSVG.classList.remove("flipped-arrow")
    previousSVG.classList.add("display-none")
    svgElement.classList.remove("display-none")
  }

  // Flips arrow
  switch (asc) {
    case true: svgElement.classList.add("flipped-arrow"); break
    case false: svgElement.classList.remove("flipped-arrow"); break
  }

  sort = targetId
  partyUrl = `${VITE_APP_BACKEND_URL}/api/party/${electionId}/${selectedProvince.value}?sort=${sort}&asc=${asc}`
  await fetchPartyData()
}

async function fetchPartyData() {
  try {
    const response = await fetch(partyUrl);

    if (response.ok) {
      parties.value = await response.json();
    }
  } catch (error) {
    console.error("Er is een fout opgetreden bij het ophalen van de data", error);
  }
}

async function fetchYearData() {
  try {
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/year`);

    if (response.ok) {
      years.value = await response.json();
    }
  } catch (error) {
    console.error("Er is een fout opgetreden bij het ophalen van de data", error);
  }
}

async function populateProps() {
  const labelsLocal: string[] = []
  const votesLocal: number[] = []

  for (const party of parties.value) {
    labelsLocal.push(party.partyName)
    votesLocal.push(party.validVotes)
  }

  labels.value = labelsLocal
  votes.value = votesLocal
}
</script>

<template>
  <div class="dropdown-wrapper">
    <select v-model="selectedProvince" class="dropdown">
      <option disabled value="">Selecteer niveau</option>
      <option
        v-for="province in provinces"
        :key="province"
        :value="province"
        @click="onClickDropdown()"
      >
        {{province}}
      </option>
    </select>
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon" aria-hidden="true">
      <path d="m6 9 6 6 6-6"></path>
    </svg>
  </div>

  <div v-if="isVisible" class="dropdown-wrapper">
    <select v-model="selectedYear" class="dropdown">
      <option disabled value="">Selecteer jaar</option>
      <option
        v-for="year in years"
        :key="year"
        :value="year"
        @click="onClickDropdown2(year)"
      >
        {{year}}
      </option>
    </select>
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon" aria-hidden="true">
      <path d="m6 9 6 6 6-6"></path>
    </svg>
  </div>

  <div v-if="isVisible2">

    <ProvinceDonutChart :labels="labels" :votes="votes" :year="selectedYear" />

    <table class="data-table">
      <thead>
      <tr>
        <th @click="onClickTable($event)" id="partyName">
          Partij
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 table-header-icon display-none" aria-hidden="true">
            <path d="m6 9 6 6 6-6"></path>
          </svg>
        </th>
        <th @click="onClickTable($event)" id="validVotes" class="sortedColumn">
          Aantal Stemmen
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 table-header-icon" aria-hidden="true">
            <path d="m6 9 6 6 6-6"></path>
          </svg>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="party in parties" :key="party.partyName">
        <td>{{ party.partyName }}</td>
        <td>{{ party.validVotes }}</td>
      </tr>
      </tbody>
    </table>
    <p v-if="parties.length === 0">Geen partijen gevonden...</p>
  </div>
</template>

<style scoped>
.flipped-arrow {
  transform: scale(1,-1) !important;
}

.display-none {
  display: none !important;
}

th {
  cursor: pointer;
}

.table-header-wrapper {
  position: relative;
}

.table-header-icon {
  width: 1em;
  display: inline;
  margin-right: 0;
  vertical-align: bottom;
  color: black;
  pointer-events: none;
}
</style>
