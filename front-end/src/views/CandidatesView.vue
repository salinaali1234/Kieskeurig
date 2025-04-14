<!-- src/views/CandidatesView.vue -->
<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue';

interface Candidate {
  id: number;
  firstName: string;
  lastName: string;
  gender: string;
  localityName: string;
}

const props = defineProps<{
  partyId: string
}>();

const candidates = ref<Candidate[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);
const partyName = ref('');

onMounted(async () => {
  try {
    // First get party info for the name
    const partyResponse = await fetch(`http://localhost:8080/api/xml/parties/${props.partyId}`);
    if (!partyResponse.ok) throw new Error('Party not found');
    const partyData = await partyResponse.json();
    if (partyData.length > 0) {
      partyName.value = partyData[0].partyName;
    }

    // Then get candidates
    const candidatesResponse = await fetch(`http://localhost:8080/api/xml/candidates/${props.partyId}`);
    if (!candidatesResponse.ok) throw new Error('Failed to load candidates');
    candidates.value = await candidatesResponse.json();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error';
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <main class="candidates-container">
    <h1>Candidates of {{ partyName }}</h1>

    <div v-if="loading" class="loading">Loading...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="candidate-list">
      <div v-for="candidate in candidates" :key="candidate.id" class="candidate-card">
        <h2>{{ candidate.firstName }} {{ candidate.lastName }}</h2>
        <p>Gender: {{ candidate.gender }}</p>
        <p>Location: {{ candidate.localityName }}</p>
      </div>
    </div>
  </main>
</template>

<style scoped>
.candidates-container {
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--primary-clr);
}

.candidate-list {
  display: grid;
  gap: 1rem;
}

.candidate-card {
  color: black;
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.candidate-card h2 {
  margin-top: 0;
  color: var(--primary-clr);
}
</style>
