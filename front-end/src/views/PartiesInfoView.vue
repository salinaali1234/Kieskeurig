<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';

interface Candidate {
  candidateId: number;
  partyId: number;
  partyName: string;
  firstName: string;
  lastName: string;
  gender: string;
  elected: boolean;
}

interface PartySeatInfo {
  partyId: number;
  partyName: string;
  seats: number;
}

const candidates = ref<Candidate[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);

const fetchCandidates = async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await fetch('http://localhost:8080/api/partiesInfo/parties');

    console.log('Response status:', response.status); // Debug log

    if (!response.ok) {
      throw new Error('Failed to load candidates');
    }

    candidates.value = await response.json();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error';
  } finally {
    loading.value = false;
  }
};

// Groepeer kandidaten op partij en tel het aantal kandidaten (zetels)
const groupedByParty = computed<PartySeatInfo[]>(() => {
  const map = new Map<number, PartySeatInfo>();

  for (const candidate of candidates.value) {
    if (!map.has(candidate.partyId)) {
      map.set(candidate.partyId, {
        partyId: candidate.partyId,
        partyName: candidate.partyName,
        seats: 1,
      });
    } else {
      map.get(candidate.partyId)!.seats++;
    }
  }

  return Array.from(map.values()).sort((a, b) => b.seats - a.seats); // sorteer op zetels aflopend
});

onMounted(() => {
  fetchCandidates();
});
</script>

<template>
  <div class="parties-container">
    <h1>Verkozen zetels per partij</h1>

    <div v-if="loading" class="loading">Laden...</div>

    <div v-else-if="error" class="error">
      {{ error }}
      <button @click="fetchCandidates" class="retry-btn">Opnieuw</button>
    </div>

    <div v-else-if="groupedByParty.length === 0" class="empty">
      Geen verkozen kandidaten gevonden
    </div>

    <div v-else class="party-grid">
      <div v-for="party in groupedByParty" :key="party.partyId" class="party-card">
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
}
</style>
