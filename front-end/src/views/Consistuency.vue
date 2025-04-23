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

const selectedView = ref("");
</script>

<template>
  <div>
    <h1 class="page-title">Kieskring {{constituencyId}}</h1>

    <table class="data-table">
      <tbody>
      <tr class="dropdown-wrapper">
        <select v-model="selectedView" class="dropdown">
          <option disabled value="">Gemeentes</option>
          <option v-for="municipality in municipalities" value="national">{{municipality[0]}}</option>

        </select>
        <span class="dropdown-icon">˅</span>
      </tr>
      </tbody>
    </table>
    <p v-if="isVisible && municipalities.length === 0">all gemeentes binnen deze kieskring inladen...</p>
  </div>
</template>

<style scoped>

.page-title {
  text-align: center;
  font-size: 2rem;
  margin-top: 40px;
  margin-bottom: 20px;
  font-weight: bold;
  color: white;
}


.dropdown-wrapper {
  position: relative;
  width: 80%;
  max-width: 600px;
  margin: 20px auto;
}

.dropdown {
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  border-radius: 8px;
  border: 1px solid var(--secondary-clr);
  background-color: #DEBFE9;
  color: black;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
}

.dropdown-icon {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2rem;
  color: black; /* zwarte pijl */
  pointer-events: none;
}

</style>
