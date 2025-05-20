<script setup lang="ts">
import { ref, onMounted } from 'vue'

import type {Account} from "@/models/Account.ts";

import { AccountsAdaptor } from '@/service/account-adaptor.ts'
import {SessionService} from "@/service/session-service.ts";


const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
const sessionService = new SessionService(`${BASE_URL}/authentication`, 'session_jwt');

const accounts = ref<Account[]>([])
const loading = ref(true)
const error = ref('')
const accountsAdaptor = new AccountsAdaptor();

onMounted(async () => {
  try {

    accounts.value = await accountsAdaptor.getAll()
    loading.value = false
  } catch (err) {
    error.value = 'Failed to load accounts'
    console.error(err)
    loading.value = false
  }
})

async function deleteAccount(id: number) {
  try {
    await accountsAdaptor.delete(id)
    accounts.value = accounts.value.filter(acc => acc.id !== id)
  } catch (err) {
    error.value = 'Failed to delete account'
    console.error(err)
  }
}
</script>

<template>
  <div class="admin-container">
    <h1>Admin Dashboard</h1>
    <p v-if="loading">Loading...</p>
    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="!loading && !error">
      <h2>User Accounts</h2>
      <table class="accounts-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="account in accounts" :key="account.id">
          <td>{{ account.id }}</td>
          <td>{{ account.name }}</td>
          <td>{{ account.email }}</td>
          <td>{{ account.role }}</td>
          <td>
            <button
              @click="deleteAccount(account.id)"
              class="delete-btn"
              :disabled="account.id === sessionService.currentAccount?.id"
            >
              Delete
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.admin-container {
  max-width: 1000px;
  margin: 2rem auto;
  padding: 2rem;
}

h1, h2 {
  color: var(--primary-clr);
  margin-bottom: 1rem;
}

.error {
  color: red;
  margin: 1rem 0;
}

.accounts-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.accounts-table th, .accounts-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: left;
}

.accounts-table th {
  background-color: var(--primary-clr);
  color: white;
}

.delete-btn {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.delete-btn:hover:not(:disabled) {
  background-color: #d32f2f;
}
</style>
