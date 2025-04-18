<script setup lang="ts">
import { ref } from "vue";
import NationalVotes from "../components/NationalVotes.vue";
import Constituencies from "../components/Constituencies.vue";
import ProvinceVotes from "@/components/ProvinceVotes.vue";

const selectedView = ref("");

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

    <div v-if="selectedView === 'national'">
      <NationalVotes />
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
</style>
