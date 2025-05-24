<script setup lang="ts">
import {ref, watch, type Ref} from "vue";
import "../assets/tableStyle.css"
import NationalDonutChart from "@/components/charts/donut-charts/NationalDonutChart.vue";

interface Party {
  name: string;
  votes: number;
}

const year = ref<string>("");
const parties = ref<Party[]>([]);
const isVisible = ref(false);
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
const props = defineProps<{ year: string }>();
const url: string = `${VITE_APP_BACKEND_URL}/api/xml/votes/parties?year=${props.year}`;

let labels: Ref<string[]> = ref([])
let votes: Ref<number[]> = ref([])

function toggleVisible() {
  isVisible.value = !isVisible.value;
}

watch(() => props.year, async (newYear) => {
  year.value = newYear;

  await fetchData(newYear);
  await populateProps();
}, { immediate: true });

async function fetchData(year: string) {
  try {
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/xml/votes/parties?year=${year}`);
    if (response.ok) {
      const data = await response.json();
      parties.value = Object.entries(data).map(([name, votes]) => ({
        name,
        votes: Number(votes),
      }));
    }
  } catch (error) {
    console.error("Fout bij ophalen data", error);
  }
}

async function populateProps() {
  const labelsLocal: string[] = []
  const votesLocal: number[] = []

  for (const party of parties.value) {
    labelsLocal.push(party.name)
    votesLocal.push(party.votes)
  }

  const sortArrays = () => {
    const list = [];
    for (let j = 0; j < labelsLocal.length; j++)
      list.push({'labels': labelsLocal[j], 'votes': votesLocal[j]});

    list.sort(function (a, b) {
      return ((a.votes > b.votes) ? -1 : ((a.votes == b.votes) ? 0 : 1));
    });

    for (let k = 0; k < list.length; k++) {
      labelsLocal[k] = list[k].labels;
      votesLocal[k] = list[k].votes;
    }
  }

  sortArrays()

  labels.value = labelsLocal
  votes.value = votesLocal
}


</script>

<template>
  <div>

    <NationalDonutChart :year="year" :votes="votes" :labels="labels" />

    <table class="data-table">
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
    <p v-if="isVisible && parties.length === 0">Geen partijen gevonden...</p>
  </div>
</template>
