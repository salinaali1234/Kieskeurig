<script setup lang="ts">
import { ref, onMounted } from "vue";

interface Party {
  name: string;
  votes: number;
}

const parties = ref<Party[]>([]);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
const url: string = `${VITE_APP_BACKEND_URL}/api/xml/votes/parties`;


onMounted(async () => {
  try {
    const response = await fetch(url);

    if (response.ok) {
      const data = await response.json();
      console.log("API Response:", data);

      if (data && typeof data === "object") {

        parties.value = Object.entries(data).map(([name, votes]) => ({
          name,
          votes: Number(votes),
        }));
      } else {
        console.error("Verkeerd formaat van API-respons", data);
      }
    } else {
      console.error("API-aanroep mislukt", response);
    }
  } catch (error) {
    console.error("Er is een fout opgetreden bij het ophalen van de data", error);
  }
});
</script>

<template>
  <div class="container">
    <h1>Nationale stemmen per partij</h1>

    <table v-if="parties.length > 0" class="party-table">
      <thead>
      <tr>
        <th>Partij</th>
        <th>Aantal Stemmen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(party, index) in parties" :key="index">
        <td>{{ party.name }}</td>
        <td>{{ party.votes }}</td>
      </tr>
      </tbody>
    </table>

    <p v-else>Geen partijen gevonden...</p>
  </div>
</template>

<style scoped>
.container {
  text-align: center;
  padding: 20px;
}

.party-table {
  width: 80%;
  max-width: 600px;
  margin: 20px auto;
  border-collapse: collapse;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.party-table th, .party-table td {
  padding: 12px;
  border-bottom: 1px solid var(--secondary-clr);
  text-align: left;
}

.party-table th {
  background-color: var(--accent-clr);
  color: black;
}

.party-table tr:hover {
  background-color: rgba(255, 255, 255, 0.15);
}

</style>
