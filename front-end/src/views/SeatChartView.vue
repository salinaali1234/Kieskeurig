<script setup lang="ts">
import { defineProps, watch, ref } from 'vue'
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js'
import { Bar } from 'vue-chartjs'

// registreer onderdelen
ChartJS.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend)

const props = defineProps<{
  // verwacht een lijst met partijen inclusief seats
  data: { partyName: string; seats: number }[]
}>()

// labels en dataset
const labels = ref<string[]>([])
const seatsData = ref<number[]>([])

watch(() => props.data, (newVal) => {
  labels.value = newVal.map(p => p.partyName)
  seatsData.value = newVal.map(p => p.seats)
}, { immediate: true })

// chart options
const options = {
  responsive: true,
  plugins: {
    legend: { display: false },
    title: {
      display: true,
      text: 'Zetels per partij'
    }
  },
  scales: {
    y: { beginAtZero: true, ticks: { stepSize: 1 } }
  }
}
</script>

<template>
  <Bar
    :data="{
      labels: labels,
      datasets: [
        {
          label: 'Zetels',
          data: seatsData,
          backgroundColor: '#3E2858'    /* je kunt hier aanpassen */
        }
      ]
    }"
    :options="options"
  />
</template>
