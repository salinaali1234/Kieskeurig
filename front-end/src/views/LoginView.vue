<script setup lang="ts">
import { ref } from 'vue';
import { SessionService } from '@/service/session-service';

const email = ref('');
const password = ref('');
const error = ref<string | null>(null);


const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
const session = new SessionService(`${BASE_URL}/authentication`, 'session_jwt');

const handleLogin = async () => {
  error.value = null; // reset foutmelding bij elke poging
  try {
    const account = await session.login(email.value, password.value);
    if (account) {
      window.location.reload();
    }
  } catch (e: never) {
    error.value = e.message || 'Inloggen mislukt. Probeer opnieuw.';
  }
};
</script>

<template>
  <form @submit.prevent="handleLogin">
    <input v-model="email" type="email" placeholder="E-mail" required autocomplete="email" />
    <input v-model="password" type="password" placeholder="Wachtwoord" required autocomplete="current-password" />
    <button type="submit">Inloggen</button>
    <p v-if="error">{{ error }}</p>
  </form>
</template>

<style scoped>

h1 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: var(--primary-clr);
}


label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.btn {
  width: 100%;
  padding: 0.75rem;
  background-color: var(--accent-clr);
  color: black;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin-top: 1rem;
}

.error {
  color: red;
  margin-top: 1rem;
  text-align: center;
}

p {
  text-align: center;
  margin-top: 1rem;
}
</style>
