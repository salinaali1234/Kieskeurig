<script setup lang="ts">
import {ref, onMounted, type Ref} from "vue";
import "../assets/tableStyle.css"
import Constituencies from "@/views/Constituencies.vue";
import NationalVotes from "@/views/NationalVotes.vue";
import router from "@/router";

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
      for(const constituency of Object.entries(data)) {
        constituencies.value.push(constituency)
      }
    }


  } catch (error) {
    console.error(error);
  }
});

const showConstituency = (id: number) => {
  router.push(`/Constituency/${id}`)
}


</script>

<template>
  <div>

    <table class="data-table">
      <tbody>
      <tr v-for="constituency in constituencies"
          :key="constituency[0]"

          @click="showConstituency(constituency[1])">
        <td>{{ constituency[0] }}</td>

      </tr>
      </tbody>
    </table>
    <p v-if="isVisible && constituencies.length === 0">Geen kieskringen gevonden...</p>
  </div>
</template>
