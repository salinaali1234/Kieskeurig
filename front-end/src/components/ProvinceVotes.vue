<script setup lang="ts">
import {ref, onMounted} from "vue";
import "../assets/tableStyle.css"
import NationalVotes from "@/components/NationalVotes.vue";

interface Party {
  name: string;
  votes: number;
}

const electionId = "TK2023" // Temp

const selectedProvince = ref("");
const provinces = ref<string[]>([]);
const parties = ref<Party[]>([]);
const isVisible = ref(false);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
const provinceUrl: string = `${VITE_APP_BACKEND_URL}/api/provinces`;
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
      const data = await response.json();
      parties.value = Object.entries(data).map(([name, votes]) => ({
        name,
        votes: Number(votes),
      }));
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
      <tr v-for="(party, index) in parties" :key="index">
        <td>{{ party.name }}</td>
        <td>{{ party.votes }}</td>
      </tr>
      </tbody>
    </table>
    <p v-if="parties.length === 0">Geen partijen gevonden...</p>
  </div>
</template>
