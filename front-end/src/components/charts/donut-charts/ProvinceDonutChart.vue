<template>
<!--  <p>{{text}}</p>-->
  <div style="height:60%;">
    <Doughnut :data="data" :options="options" />
  </div>
</template>

<script lang="ts">
  import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
  import { Doughnut } from 'vue-chartjs'

  ChartJS.register(ArcElement, Tooltip, Legend)

  export default {
    name: 'ChartTest',
    components: {
      Doughnut
    },
    props: {
      labels: {
        type: Array as () => string[],
        required: true
      },
      votes: {
        type: Array as () => number[],
        required: true
      }
    },
    computed: {
      data() {
        return {
          labels: this.labels,
          datasets: [
            {
              backgroundColor: ['#41B883', '#E46651', '#00D8FF', '#DD1B16'],
              data: this.votes
            }
          ]
        }
      },
      options() {
        return {
          responsive: true,
          maintainAspectRatio: false
        }
      }
    }
  }
</script>
