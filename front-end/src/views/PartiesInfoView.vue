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

// Partij data
const partiesList = ref<PartyWithInfo[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);
const sortOrder = ref<'seats-desc' | 'seats-asc' | 'alphabetical'>('seats-desc');

// Kandidaten modal
const showModal = ref(false);
const currentCandidates = ref<Candidate[]>([]);
const currentPartyName = ref('');
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
    currentPartyName.value = partyName;
    showModal.value = true;

    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/partiesInfo/candidates/${partyId}`);
    if (!response.ok) throw new Error('Kon kandidaten niet laden');
    currentCandidates.value = await response.json();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Fout bij laden kandidaten';
  } finally {
    candidatesLoading.value = false;
  }
};

watch(sortOrder, fetchParties);
onMounted(fetchParties);
</script>

<template>
  <div class="parties-container">
    <!-- Partij lijst header -->
    <div class="header-container">
      <h1>Verkozen zetels per partij</h1>
      <div class="sort-container">
        <label for="sort-select">Sorteer op:</label>
        <select id="sort-select" v-model="sortOrder" class="sort-select">
          <option v-for="option in sortOptions" :key="option.value" :value="option.value">
            {{ option.label }}
          </option>
        </select>
      </div>
    </div>

    <!-- Loading/error states -->
    <div v-if="loading" class="loading">Laden...</div>
    <div v-else-if="error" class="error">
      {{ error }}
      <button @click="fetchParties" class="retry-btn">Opnieuw</button>
    </div>
    <div v-else-if="partiesList.length === 0" class="empty">Geen partijen gevonden</div>

    <!-- Partijen grid -->
    <div v-else class="party-grid">
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

    <!-- Kandidaten Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-content">
        <button class="close-btn" @click="showModal = false">×</button>
        <h2>Kandidaten van {{ currentPartyName }}</h2>

        <div v-if="candidatesLoading" class="loading">Kandidaten laden...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <div v-else-if="currentCandidates.length === 0" class="empty">
          Geen kandidaten gevonden voor deze partij
        </div>

        <div v-else class="candidates-list">
          <div v-for="candidate in currentCandidates" :key="candidate.id" class="candidate-item">
            <h3>{{ candidate.firstName }} {{ candidate.lastName }}</h3>
            <p>Geslacht: {{ candidate.gender }}</p>
            <p>Locatie: {{ candidate.localityName }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
.parties-container {
  color: black;
  padding: 2rem;
  max-width: 900px;
  margin: 0 auto;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  gap: 1rem;
  color: white; /* Nu werkt dit wel */
  background-color: #3E2858;
  padding: 1rem;
  border-radius: 8px;
}

.sort-container {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.sort-select {
  border-radius: 4px;
  border: 1px solid #d1a5e6;
  background-color: #DEBFE9;
  font-size: 0.9rem;
  cursor: pointer;
  color: #2c3e50;
  transition: all 0.3s ease;
  appearance: none;
  background-repeat: no-repeat;
  background-position: right 0.7rem center;
  background-size: 1rem;
  padding: 0.5rem 2rem 0.5rem 1rem;
}

.sort-select:hover {
  background-color: #d1a5e6;
}

.party-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.party-card {
  background: white;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.party-card:hover {
  transform: translateY(-3px);
}

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
  display: flex;
  justify-content: space-between;
  align-items: center;
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

/* Responsive aanpassingen */
@media (max-width: 600px) {
  .header-container {
    flex-direction: column;
  }

  .sort-container {
    width: 100%;
    margin-top: 1rem;
  }

  .sort-select {
    width: 100%;
  }
}
</style>
