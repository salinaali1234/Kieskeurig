<script setup lang="ts">
import {ref, onMounted} from "vue";
import "../assets/tableStyle.css"

interface Party {
  partyName: string;
  validVotes: number;
}

const electionId = "TK2023" // Temp
const sort = ref("validVotes")
const asc = ref(false)

const selectedProvince = ref("");
const provinces = ref<string[]>([]);
const parties = ref<Party[]>([]);
const isVisible = ref(false);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
const provinceUrl: string = `${VITE_APP_BACKEND_URL}/api/provinces?sort=${sort.value}&asc=${asc.value}`;
let partyUrl: string = "";

function toggleVisible() {
  isVisible.value = !isVisible.value;
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

async function onClick(province: string) {
  partyUrl = `${VITE_APP_BACKEND_URL}/api/party/${electionId}/${province}`;
  isVisible.value = true;

  try {
    const response = await fetch(partyUrl);

    if (response.ok) {
      parties.value = await response.json();
    }
  } catch (error) {
    console.error("Er is een fout opgetreden bij het ophalen van de data", error);
  }
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
        @click="onClick(province)"
      >
        {{province}}
      </option>
    </select>
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon" aria-hidden="true">
      <path d="m6 9 6 6 6-6"></path>
    </svg>
  </div>

  <div v-if="isVisible">
    <table class="data-table">
      <thead>
      <tr>
        <th>Partij</th>
        <th>Aantal Stemmen</th>
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
