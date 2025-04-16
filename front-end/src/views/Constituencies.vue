<script setup lang="ts">
import { ref, onMounted } from "vue";
import "../assets/tableStyle.css"

interface Constituency {
  name: string;
}

const constituencies = ref<Constituency[]>([]);
const isVisible = ref(false);
const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const url = `${backendUrl}/api/constituencies/all/Constituencies/0`;

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

onMounted(async () => {
  try {
    const response = await fetch(url);
    if (response.ok) {
      const data = await response.json();
      constituencies.value = Object.entries(data).map(([name]) => ({ name }));
    }
  } catch (error) {
    console.error("Fout bij ophalen kieskringen:", error);
  }
});
</script>

<template>
  <div>

    <table class="data-table">
      <tbody>
      <tr v-for="(constituency, index) in constituencies" :key="index">
        <td>{{ constituency.name }}</td>
      </tr>
      </tbody>
    </table>
    <p v-if="isVisible && constituencies.length === 0">Geen kieskringen gevonden...</p>
  </div>
</template>
