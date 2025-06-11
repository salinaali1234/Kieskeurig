<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { AccountsAdaptor } from '@/service/account-adaptor.ts'
import type {Account} from "@/models/Account.ts";


const router = useRouter()
const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
const accountsAdaptor = new AccountsAdaptor();


const name = ref('')
const email = ref('')
const password = ref('')
const error = ref('')
const success = ref('')

async function handleRegister() {
  try {
    const newAccount: Account = {
      id: 0,
      name: name.value,
      email: email.value,
      password: password.value,
      role: 'Regular User'
    };
    console.log('Sending account:', newAccount);
    const account = await accountsAdaptor.save(newAccount);
    if (account) {
      success.value = 'Registratie gelukt! Je kunt nu inloggen.';
      error.value = '';
    } else {
      error.value = 'Account kon niet worden aangemaakt.';
    }
  } catch (err) {
    error.value = 'Registratie mislukt. Probeer opnieuw.';
    console.error(err);
  }
}
</script>

<template>
  <div class="register-container">
    <h1>Registreren</h1>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">Naam:</label>
        <input v-model="name" type="text" id="name" required>
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input v-model="email" type="email" id="email" required>
      </div>
      <div class="form-group">
        <label for="password">Wachtwoord:</label>
        <input v-model="password" type="password" id="password" required>
      </div>
      <button type="submit" class="btn">Registreren</button>
      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>
      <p>Al een account? <RouterLink to="/login">Login hier</RouterLink></p>
    </form>
  </div>
</template>

<style scoped>
.register-container {
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

.success {
  color: green;
  margin-top: 1rem;
  text-align: center;
}

p {
  text-align: center;
  margin-top: 1rem;
  color: #2c3e50;
}
</style>
