<script setup lang="ts">
import { objectToString } from "@vue/shared";
import { ref, onMounted } from "vue";

interface Constituency {
  id: number;
  name: string;

}

const constituencies = ref<Constituency[]>([]);


onMounted(async () => {
  try {
    const response = await fetch("http://localhost:8080/api/xml/getAll");

    if (response.ok) {
      const data = await response.json();
      console.log("API Response:", data);

      if (data && typeof data === "object") {

        constituencies.value = Object.entries(data).map(([id, name]) => ({
          id: Number(id),
          name
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
    <h1>kieskringen </h1>

    <table v-if="constituencies.length > 0" class="party-table">
      <thead>
      <tr>
        <th>Id</th>
        <th>Constituency</th>
      </tr>
      </thead>
      <tbody>

      </tbody>
    </table>

    <p v-else>Geen kieskringen gevonden...</p>
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
