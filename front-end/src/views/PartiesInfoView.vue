<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';

const VITE_APP_BACKEND_URL = import.meta.env.VITE_APP_BACKEND_URL;

interface PartyWithInfo {
  partyId: number;
  partyName: string;
  seats: number;
}

interface Candidate {
  id: number;
  firstName: string;
  lastName: string;
  gender: string;
  localityName: string;
}

const partiesList = ref<PartyWithInfo[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);
const sortOrder = ref<'seats-desc' | 'seats-asc' | 'alphabetical'>('seats-desc');

const currentPartyId = ref<number | null>(null);
const currentPartyName = ref('');
const currentCandidates = ref<Candidate[]>([]);
const candidatesLoading = ref(false);

const sortOptions = [
  { value: 'seats-desc', label: 'Zetels (hoog → laag)' },
  { value: 'seats-asc', label: 'Zetels (laag → hoog)' },
  { value: 'alphabetical', label: 'Alfabetisch (A → Z)' }
];

const fetchParties = async () => {
  try {
    loading.value = true;
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/partiesInfo/parties?sort=${sortOrder.value}`);
    if (!response.ok) throw new Error('Kon partijen niet laden');
    partiesList.value = await response.json();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Onbekende fout';
  } finally {
    loading.value = false;
  }
};

const showCandidates = async (partyId: number, partyName: string) => {
  try {
    candidatesLoading.value = true;
    currentPartyId.value = partyId;
    currentPartyName.value = partyName;

    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/partiesInfo/candidates/${partyId}`);
    if (!response.ok) throw new Error('Kon kandidaten niet laden');
    currentCandidates.value = await response.json();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Fout bij laden kandidaten';
  } finally {
    candidatesLoading.value = false;
  }
};

const resetView = () => {
  currentPartyId.value = null;
  currentCandidates.value = [];
};

watch(sortOrder, fetchParties);
onMounted(fetchParties);
</script>

<template>
  <div class="parties-container">
    <div class="header-container">
      <h1>Verkozen zetels per partij</h1>
      <div class="sort-container" v-if="!currentPartyId">
        <label for="sort-select">Sorteer op:</label>
        <select id="sort-select" v-model="sortOrder" class="sort-select">
          <option v-for="option in sortOptions" :key="option.value" :value="option.value">
            {{ option.label }}
          </option>
        </select>
      </div>
    </div>

    <div v-if="loading" class="loading">Laden...</div>
    <div v-else-if="error" class="error">
      {{ error }}
      <button @click="fetchParties" class="retry-btn">Opnieuw</button>
    </div>

    <!-- 🔁 Partijenlijst -->
    <div v-if="!currentPartyId" class="party-grid">
      <div
        v-for="party in partiesList"
        :key="party.partyId"
        class="party-card"
        @click="showCandidates(party.partyId, party.partyName)"
      >
        <h2>{{ party.partyName }}</h2>
        <p><strong>Zetels:</strong> {{ party.seats }}</p>
      </div>
    </div>

    <!-- 👤 Kandidatenlijst -->
    <div v-else>
      <button @click="resetView" class="back-btn">← Terug naar partijen</button>
      <h1>Kandidaten van {{ currentPartyName }}</h1>

      <div v-if="candidatesLoading" class="loading">Kandidaten laden...</div>
      <div v-else-if="currentCandidates.length === 0" class="empty">Geen kandidaten gevonden</div>

      <div class="candidates-list">
        <div v-for="candidate in currentCandidates" :key="candidate.id" class="candidate-item">
          <h3>{{ candidate.firstName }} {{ candidate.lastName }}</h3>
          <p>Geslacht: {{ candidate.gender }}</p>
          <p>Locatie: {{ candidate.localityName }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Algemeen layout */
.parties-container {
  padding: 2rem;
  max-width: 900px;
  margin: 0 auto;
  color: black; /* fallback textkleur */
}

/* Terug-knop */
.back-btn {
  display: inline-block;
  background: #DEBFE9;
  color: #000;
  border: 1px solid #ccc;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 1rem;
  transition: background 0.2s;
}
.back-btn:hover {
  background: #f5f5f5;
}

/* Partij-kaartjes */
.party-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}
.party-card {
  background: white;
  color: black;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}
.party-card:hover {
  transform: translateY(-3px);
}

/* Kandidatenlijst */
.candidates-list {
  display: grid;
  gap: 1.5rem;
  margin-top: 1rem;
}
.candidate-item {
  background: white;
  color: black;
  padding: 1.2rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}
.candidate-item:hover {
  transform: translateY(-2px);
}

/* Header-teksten */
h1, h2, h3 {
  color: #000;
}

/* Overige states */
.loading {
  text-align: center;
  padding: 2rem;
  font-style: italic;
  color: #666;
}
.error {
  background: #ffebee;
  color: #d32f2f;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}
.retry-btn {
  background: #1976d2;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}
.retry-btn:hover {
  background: #1565c0;
}

/* Header-container */
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #3E2858;
  padding: 1rem;
  border-radius: 8px;
}
.header-container h1 {
  color: white;
}

/* Sorteer-select */
.sort-select {
  border-radius: 4px;
  border: 1px solid #d1a5e6;
  background-color: #DEBFE9;
  color: #2c3e50;
  padding: 0.5rem 1rem;
}
.sort-container label {
  color: white;
}
/* Kandidaten-view header */
.back-btn + h1 {
  color: white;
}
</style>
