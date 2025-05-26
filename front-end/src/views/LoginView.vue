<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { SessionService } from '@/service/session-service';

const email = ref('');
const password = ref('');
const error = ref<string | null>(null);
const router = useRouter();

const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
const session = new SessionService(`${BASE_URL}/authentication`, 'session_jwt');

const handleLogin = async () => {
  const account = await session.login(email.value, password.value);
  if (account) {
    router.push('/');
  } else {
    error.value = 'Inloggen mislukt. Probeer opnieuw.';
  }
};
</script>

<template>
  <form @submit.prevent="handleLogin">
    <input v-model="email" type="email" placeholder="E-mail" required />
    <input v-model="password" type="password" placeholder="Wachtwoord" required />
    <button type="submit">Inloggen</button>
    <p v-if="error">{{ error }}</p>
  </form>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: var(--primary-clr);
}

.form-group {
  margin-bottom: 1rem;
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
