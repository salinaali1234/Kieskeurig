<!-- src/views/PartiesView.vue -->
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

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/xml');
    if (!response.ok) {
      throw new Error('Failed to load parties');
    }
    const data = await response.json();
    if (data.length > 0 && data[0].parties) {
      parties.value = data[0].parties;
    }
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error';
  } finally {
    loading.value = false;
  }
});

const navigateToCandidates = (partyId: number) => {
  router.push(`/parties/${partyId}/candidates`);
};
</script>

<template>
  <main class="parties-container">
    <h1>Political Parties</h1>

    <div v-if="loading" class="loading">Loading...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="party-grid">
      <div
        v-for="party in parties"
        :key="party.partyId"
        class="party-card"
        @click="navigateToCandidates(party.partyId)"
      >
        <h2>{{ party.partyName }}</h2>
        <p>{{ party.candidates.length }} candidates</p>
      </div>
    </div>
  </main>
</template>

<style scoped>
.parties-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--primary-clr);
}

.loading, .error {
  text-align: center;
  padding: 2rem;
}

.error {
  color: #ff4444;
}

.party-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
}

.party-card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.party-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.party-card h2 {
  margin-top: 0;
  color: var(--primary-clr);
}

.party-card p {
  color: #666;
  margin-bottom: 0;
}
</style>
