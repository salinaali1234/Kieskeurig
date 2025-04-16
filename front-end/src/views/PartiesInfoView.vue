<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

interface Party {
  partyId: number;
  partyName: string;
  candidates: any[];
}

const parties = ref<Party[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);
const router = useRouter();

const fetchParties = async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await fetch('http://localhost:8080/api/partiesInfo/parties');

    console.log('Response status:', response.status); // Debug log

    if (!response.ok) {
      throw new Error(`Server error: ${response.status}`);
    }

    const data = await response.json();
    console.log('Received data:', data); // Debug log


    parties.value = data;

  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error';
    console.error('Fetch error:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchParties();
});

const showCandidates = (partyId: number) => {
  router.push(`/parties/${partyId}/candidates`);
};
</script>

<template>
  <div class="parties-container">
    <h1>Politieke partijen</h1>

    <div v-if="loading" class="loading">Loading parties...</div>

    <div v-else-if="error" class="error">
      Error loading parties: {{ error }}
      <button @click="fetchParties" class="retry-btn">Try Again</button>
    </div>

    <div v-else-if="parties.length === 0" class="empty">
      No parties found
    </div>

    <div v-else class="party-grid">
      <div
        v-for="party in parties"
        :key="party.partyId"
        class="party-card"
        @click="showCandidates(party.partyId)"
      >
        <h2>{{ party.partyName }}</h2>
        <p>{{ party.candidates?.length || 0 }} candidates</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.parties-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
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

.party-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.party-card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s;
}

.party-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.party-card h2 {
  margin-top: 0;
  color: #1976d2;
}
</style>
