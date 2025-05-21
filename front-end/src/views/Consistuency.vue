<script setup lang="ts">
import {ref, onMounted, type Ref, computed} from "vue";
import "../assets/tableStyle.css"

import { useRoute} from "vue-router";

const route = useRoute();
const constituencyId = computed(() => route.params.constituencyId);
const constituencyName = computed(() => route.params.constituencyName);

const municipalities: Ref<any[]> = ref([]);
const results: Ref<any[]> = ref([]);
const isVisible = ref(false);
const sortDirection = ref<'asc' | 'desc' | null>(null); // asc = A-Z, desc = Z-A, null = original
const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const url = `${backendUrl}/api/municipalities/all/${constituencyId.value}`;
const urlVotes = `${backendUrl}/api/constituencies/Info/Arnhem`

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

onMounted(async () => {
  try {
    const response = await fetch(url);
    if (response.ok) {
      const data= await response.json();
      for(const municipality of Object.entries(data)) {
        municipalities.value.push(municipality)
      }
    }

  } catch (error) {
    console.error(error);
  }
  try {
    const Responsevotes = await fetch(urlVotes)
    if (Responsevotes.ok){
      const votes = await Responsevotes.json()
      for (const result of Object.entries(votes)){
        results.value.push(result)
      }
    }
  }catch (error) {
    console.error(error)
  }
});

const selectedView = ref("");

const displayedVotes = computed(() => {
  if (sortDirection.value === 'asc') {
    return [...results.value].sort((a, b) =>
      a[0].localeCompare(b[0])
    );
  }
  if (sortDirection.value === 'desc') {
    return [...results.value].sort((a, b) =>
      b[0].localeCompare(a[0])
    );
  }
  return results.value;
});

const displayedMunicipalities = computed(() => {
  if (sortDirection.value === 'asc') {
    return [...municipalities.value].sort((a, b) =>
      a[0].localeCompare(b[0])
    );
  }
  if (sortDirection.value === 'desc') {
    return [...municipalities.value].sort((a, b) =>
      b[0].localeCompare(a[0])
    );
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
    <h1 class="page-title">Kieskring {{constituencyName}}</h1>
    <thead class="sortingButton">
    <tr>
      <th @click="toggleSortByName()" style="cursor: pointer;" >
        <span v-if="sortDirection == null">niet gesorteerd</span>
        <span v-if="sortDirection === 'asc'">A-Z▼ ▲</span>
        <span v-else-if="sortDirection === 'desc'">Z-A ▲ ▼</span>
      </th>
    </tr>
    </thead>
    <table class="data-table">
      <tbody>
      <tr class="dropdown-wrapper">
        <select v-model="selectedView" class="dropdown">
          <option disabled value="">Gemeentes</option>
          <option v-for="municipality in displayedMunicipalities" value="national">{{municipality[0]}}</option>

        </select>
        <span class="dropdown-icon">˅</span>
      </tr>
      </tbody>
    </table>

    <table class="data-table">
      <thead>
      <tr>
        <th>Partij</th>
        <th>Aantal Stemmen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="results in displayedVotes ">
        <td>{{ results[0] }}</td>
        <td>{{ results[1]}}</td>
      </tr>
      </tbody>
    </table>
    <p v-if="isVisible && municipalities.length === 0">all gemeentes binnen deze kieskring inladen...</p>
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
  color: black; /* zwarte pijl */
  pointer-events: none;
}

</style>
