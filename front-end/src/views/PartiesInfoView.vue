<script setup lang="ts">
import {ref, onMounted, watch} from 'vue';
import { useRoute } from "vue-router";

const route = useRoute()
const parties: string | string[] = route.params.parties
const partyId: string | string[] = route.params.partyid
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL

interface PartyWithInfo {
  partyId: number;
  partyName: string;
  seats: number;
}

const partiesList = ref<PartyWithInfo[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);
const sortOrder = ref<'seats-desc' | 'seats-asc' | 'alphabetical' | 'alphabetical-reverse'>('seats-desc');

const sortOptions = [
  { value: 'seats-desc', label: 'Zetels (hoog → laag)' },
  { value: 'seats-asc', label: 'Zetels (laag → hoog)' },
  { value: 'alphabetical', label: 'Alfabetisch (A → Z)' },
  { value: 'alphabetical-reverse', label: 'Alfabetisch (A <- Z)' }
];

const fetchParties = async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/partiesInfo/parties?sort=${sortOrder.value}`);

    if (!response.ok) {
      throw new Error('Failed to load parties');
    }

    partiesList.value = await response.json();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error';
  } finally {
    loading.value = false;
  }
};


watch(sortOrder, () => {
  fetchParties();
});

onMounted(() => {
  fetchParties();
});
</script>

<template>
  <div class="parties-container">
    <div class="header-container">
      <h1>Verkozen zetels per partij</h1>
      <div class="sort-container">
        <label for="sort-select">Sorteer op:</label>
        <select
          id="sort-select"
          v-model="sortOrder"
          class="sort-select"
        >
          <option
            v-for="option in sortOptions"
            :key="option.value"
            :value="option.value"
          >
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

    <div v-else-if="partiesList.length === 0" class="empty">
      Geen partijen gevonden
    </div>

    <div v-else class="party-grid">
      <div v-for="party in partiesList" :key="party.partyId" class="party-card">
        <h2>{{ party.partyName }}</h2>
        <p><strong>Zetels:</strong> {{ party.seats }}</p>
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
