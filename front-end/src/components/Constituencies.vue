<script setup lang="ts">
import {ref, onMounted, type Ref, computed} from "vue";
import "../assets/tableStyle.css"
import router from "@/router";
import Consistuency from "@/views/Consistuency.vue";

interface Constituency {
  id: number;
  name: string;
}

const constituencies: Ref<any[]> = ref([]);
let constituencyName: Ref<any[]> = ref([])
const isVisible = ref(false);
const sortDirection = ref<'asc' | 'desc' | null>(null); // asc = A-Z, desc = Z-A, null = original

const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const url = `${backendUrl}/api/constituencies/all`;

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

function showsdataIfclicked() {
  console.log("hello there")

  const consttuency = constituencyName.value
  console.log(consttuency)

  if (consttuency == null ){
    console.log("there is no name")
  }else {
    console.log("there is a name")
  }
}
const showConstituency = (id: number, name: string) => {
  router.push(`/Constituency/${id}/${name}`)
}

const displayedConstituencies = computed(() => {
  if (sortDirection.value === 'asc') {
    return [...constituencies.value].sort((a, b) =>
      a[0].localeCompare(b[0])
    );
  }
  if (sortDirection.value === 'desc') {
    return [...constituencies.value].sort((a, b) =>
      b[0].localeCompare(a[0])
    );
  }
  return constituencies.value;
});

function toggleSortByName() {
  if (sortDirection.value === null) {
    sortDirection.value = 'asc';
  } else if (sortDirection.value === 'asc') {
    sortDirection.value = 'desc';
  } else {
    sortDirection.value = null;
  }
}


</script>

<template>
  <div>

    <thead class="sortingButton">
    <tr>
      <th @click="toggleSortByName()" style="cursor: pointer;" >
        <span v-if="sortDirection == null">niet gesorteerd</span>
        <span v-if="sortDirection === 'asc'">A-Z▼ ▲</span>
        <span v-else-if="sortDirection === 'desc'">Z-A ▲ ▼</span>
      </th>
    </tr>
    </thead>

    <table class="data-table">
      <tbody>
      <tr v-for="constituency in displayedConstituencies"
          :key="constituency[0]"

          @click="showConstituency(constituency[1], constituency[0])">
        <td>{{ constituency[0] }}</td>

      </tr>

      </tbody>
    </table>
    <p v-if="isVisible && constituencies.length === 0">Geen kieskringen gevonden...</p>
  </div>
</template>

<style>
.sortingButton{
  margin-left: 40px;
}

</style>

