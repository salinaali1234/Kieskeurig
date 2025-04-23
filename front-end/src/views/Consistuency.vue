<script setup lang="ts">
import {ref, onMounted, type Ref, computed} from "vue";
import "../assets/tableStyle.css"

import router from "@/router";
import { useRoute} from "vue-router";

const route = useRoute();
const constituencyId = computed(() => route.params.constituencyId);

const municipalities: Ref<any[]> = ref([]);
const isVisible = ref(false);
const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const url = `${backendUrl}/api/constituencies/all/municipalities/${constituencyId.value}`;

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

onMounted(async () => {
  try {
    const response = await fetch(url);
    if (response.ok) {
      const data= await response.json();
      for(const municipality of Object.entries(data)) {
        municipalities.value.push(municipality)
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
      <tr v-for="municipality in municipalities"
          :key="municipality[0]"
          @click="showConstituency(municipality[1])">
        <td>{{ municipality[0] }}</td>

      </tr>
      </tbody>
    </table>
    <p v-if="isVisible && municipalities.length === 0">all gemeentes binnen deze kieskring inladen...</p>
  </div>
</template>
