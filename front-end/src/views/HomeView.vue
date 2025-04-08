<!-- src/views/HomeView.vue -->
<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToParties = () => {
  router.push('/parties');
};
</script>

<template>
  <main class="home-container">
    <h1>Welcome to KiesKeurig</h1>
    <p>Discover all political parties and their candidates</p>
    <button @click="navigateToParties" class="cta-button">View All Parties</button>
  </main>
</template>

<style scoped>
.home-container {
  text-align: center;
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

h1 {
  color: var(--primary-clr);
  margin-bottom: 1rem;
}

p {
  color: #666;
  margin-bottom: 2rem;
}

.cta-button {
  background-color: var(--accent-clr);
  color: black;
  border: none;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.cta-button:hover {
  background-color: #e6b800;
}
</style>
