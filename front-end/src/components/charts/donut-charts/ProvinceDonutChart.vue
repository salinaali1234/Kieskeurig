<template>
  <div style="height:60vh;">
    <Doughnut :data="data" :options="options" />
  </div>
</template>

<script lang="ts">
  import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
  import { Doughnut } from 'vue-chartjs'

  ChartJS.register(ArcElement, Tooltip, Legend)

  export default {
    name: 'ProvinceDonutChart',
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
        const votePercentages: string[] = ((): string[] => {
          const totalVotes: number = this.votes.reduce((partialSum, a) => partialSum + a, 0)
          const votePercentages: string[] = []

          for (const vote of this.votes) {
            const percentage: string = ((vote / totalVotes) * 100).toFixed(2)
            if (parseFloat(percentage) < 1) {
              votePercentages.push(percentage + "%")
            }
          }

          return votePercentages
        })()

        const initialPercentageLength: number = votePercentages.length
        const votes: number[] = this.votes.slice(0, initialPercentageLength)
        const labels: string[] = this.labels.slice(0, initialPercentageLength)

        const votesOther: number = this.votes.slice(initialPercentageLength).reduce((partialSum, a) => partialSum + a, 0)
        votes.push(votesOther)
        labels.push("Overig")

        const length: number = votes.length

        const colors: string[] = ((): string[] => {
          function rgbToHex(r: number, g: number, b: number): string {
            return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
          }

          const r: number = 100
          const g: number = 200
          const bStart: number = 0
          const bEnd: number = 255
          const bInterval: number = Math.floor((bEnd - bStart) / length)
          const rgbValues: string[] = []

          let bCurrent: number = bStart
          for (let i: number = 0; i < length; i++) {
            rgbValues.unshift(rgbToHex(r, g, bCurrent))

            bCurrent += bInterval
          }

          return rgbValues
        })()

        return {
          labels: labels,
          datasets: [
            {
              backgroundColor: colors,
              data: votes,
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
