<script setup lang="ts">
import {ref, onMounted, type Ref} from "vue";
import "../assets/tableStyle.css"
import Constituencies from "@/views/Constituencies.vue";

interface Constituency {
  id: number;
  name: string;
}

const constituencies: Ref<any[]> = ref([]);
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
      const data= await response.json();
      console.log(data)


      for(const constituency of Object.entries(data)) {
        constituencies.value.push(constituency)
      }
      console.log(constituencies)
    }


  } catch (error) {
    console.error(error);
  }
});
</script>

<template>
  <div>

    <table class="data-table">
      <tbody>
      <tr v-for="constituency in constituencies" :key="constituency[0]">
        <td @click="console.log(constituency[1])">{{ constituency[0] }}</td>

      </tr>
      </tbody>
    </table>
    <p v-if="isVisible && constituencies.length === 0">Geen kieskringen gevonden...</p>
  </div>
</template>
