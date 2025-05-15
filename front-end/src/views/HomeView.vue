<script setup lang="ts">
import { useRouter } from 'vue-router'
import sessionService from '@/service/session-singleton.ts';
import {SessionService} from "@/service/session-service.ts";

const router = useRouter()

const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
const session = new SessionService(`${BASE_URL}/authentication`, 'session_jwt');

const navigateToParties = () => {
  router.push('/parties')
}
</script>

<template>
  <main class="home-container">
    <h1>Welcome to KiesKeurig</h1>
    <p>Discover all political parties and their candidates</p>

    <div v-if="sessionService.isAuthenticated()">
      <p>Welcome back, {{ sessionService.currentAccount?.name }}!</p>
      <button v-if="sessionService.isAdmin()" @click="router.push('/admin')" class="admin-button">
        Admin Dashboard
      </button>
    </div>

    <button @click="navigateToParties" class="cta-button">View All Parties</button>

    <div v-if="!sessionService.isAuthenticated()" class="auth-buttons">
      <RouterLink to="/login" class="auth-button">Login</RouterLink>
      <RouterLink to="/register" class="auth-button">Register</RouterLink>
    </div>
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
  margin: 0.5rem;
}

.admin-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
  margin: 0.5rem;
}

.auth-buttons {
  margin-top: 2rem;
}

.auth-button {
  display: inline-block;
  background-color: var(--primary-clr);
  color: white;
  padding: 0.75rem 1.5rem;
  margin: 0.5rem;
  border-radius: 5px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.3s;
}

.auth-button:hover {
  background-color: var(--accent-clr);
  color: black;
}

.cta-button:hover, .admin-button:hover {
  opacity: 0.9;
}
</style>
