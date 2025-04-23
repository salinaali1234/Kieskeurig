<script setup lang="ts">
import { ref, onMounted } from 'vue';

const candidates = ref([]);
const loading = ref(true);
const error = ref<string | null>(null);

const fetchCandidates = async () => {
  try {
    loading.value = true;
    error.value = null;

    const response = await fetch('http://localhost:8080/api/partiesInfo/candidates/resultaat');
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

onMounted(() => {
  fetchCandidates();
});
</script>

<template>
  <div v-if="loading" class="loading">Loading candidates...</div>

  <div v-else-if="error" class="error">
    Error loading candidates: {{ error }}
    <button @click="fetchCandidates" class="retry-btn">Try Again</button>
  </div>

  <div v-else>
    <div v-for="candidate in candidates" :key="candidate.id" class="candidate-card">
      <h3>{{ candidate.firstName }} {{ candidate.lastName }}</h3>
      <p><strong>Partij:</strong> {{ candidate.partyName }}</p>
      <p><strong>Geslacht:</strong> {{ candidate.gender }}</p>
    </div>
  </div>
</template>

<style scoped>
.loading {
  text-align: center;
  padding: 2rem;
  font-style: italic;
  color: #666;
}
.candidate-card {
  background: white;
  padding: 1.2rem;
  margin-bottom: 1rem;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
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
