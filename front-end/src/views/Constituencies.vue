<script setup lang="ts">

import { ref, onMounted } from "vue";

interface Constituency {
  name: string;

}

const constituencies = ref<Constituency[]>([]);
const isVisible = ref(false)

function toggleVisible() {
  isVisible.value = !isVisible.value
}

onMounted(async () => {
  try {
    const response = await fetch("http://localhost:8080/api/constituencies/all/Constituencies");


    if (response.ok) {
      const data = await response.json();
      console.log("API Response:", data);

      if (data && typeof data === "object") {

        constituencies.value = Object.entries(data).map(([name]) => ({
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
    <h1>Kieskringen</h1>

    <div class="header-row">
      <span>Kieskring</span>
      <button class="downarrolist" @click="toggleVisible">
        {{ isVisible ? '˄' : '˅' }}
      </button>
    </div>

    <table v-show="isVisible && constituencies.length > 0" class="party-table">
      <tbody>
      <tr v-for="(constituency, index) in constituencies" :key="index">
        <td>{{ constituency.name }}</td>
      </tr>
      </tbody>
    </table>


  </div>
</template>


<style scoped>
.container {
  text-align: center;
  padding: 20px;
}

.downarrolist {
  padding-right: 12px;
  margin: 0;

  font-size: 2rem;
  background: var(--secondary-clr);
  border: none;
  color: black;
  cursor: pointer;
}

.party-table {
  width: 80%;
  max-width: 600px;
  margin-right: auto;
  margin-left: auto;
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
  background-color: var(--secondary-clr);
  color: black;
}

.party-table tr:hover {
  background-color: rgba(255, 255, 255, 0.15);
}

.header-row {
  color: black;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 80%;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
  padding-left: 12px;
  border-collapse: collapse;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  background-color:#DEBFE9;
  border-radius: 8px;
  overflow: hidden;

}


</style>
