<script setup lang="ts">
import { ref } from "vue";
import NationalVotes from "../components/NationalVotes.vue";
import Constituencies from "../components/Constituencies.vue";
import ProvinceVotes from "../components/ProvinceVotes.vue";

const selectedView = ref("");
const selectedYear = ref("2023");

</script>

<template>
  <div class="container">
    <h1 class="page-title">Statistieken</h1>

    <div class="dropdown-wrapper">
      <select v-model="selectedView" class="dropdown">
        <option disabled value="">Selecteer niveau</option>
        <option value="national">Nationaal</option>
        <option value="provinces">Provincies</option>
        <option value="constituencies">Kieskringen</option>
      </select>
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon" aria-hidden="true">
        <path d="m6 9 6 6 6-6"></path>
      </svg>
    </div>


    <div
      v-if="selectedView === 'national'"
      class="dropdown-wrapper"
    >
      <select v-model="selectedYear" class="dropdown">
        <option disabled value="">Selecteer jaar</option>
        <option value="2023">2023</option>
        <option value="2021">2021</option>
      </select>
      <span class="dropdown-icon">˅</span>
    </div>


    <div v-if="selectedView === 'national'">
      <NationalVotes v-if="selectedView === 'national'" :year="selectedYear" />
    </div>

    <div v-else-if="selectedView === 'provinces'">
      <ProvinceVotes />
    </div>

    <div v-else-if="selectedView === 'constituencies'">
      <Constituencies />
    </div>
  </div>
</template>

<style scoped>

.page-title {
  text-align: center;
  font-size: 2rem;
  margin-top: 40px;
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
