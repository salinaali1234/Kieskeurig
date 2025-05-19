<template>
  <div class="container">
    <h1 style="text-align: center;">Stemresultaten {{province}} {{year}}</h1>
    <div style="height:60vh;">
      <Doughnut :data="data" :options="options" />
    </div>
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
      },
      province: {
        type: String,
        required: true
      },
      year: {
        type: String,
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

        const length: number = votePercentages.length
        const votes: number[] = this.votes.slice(0, length)
        const labels: string[] = this.labels.slice(0, length)

        const votesOther: number = this.votes.slice(length).reduce((partialSum, a) => partialSum + a, 0)
        votes.push(votesOther)
        labels.push("Overig")

        const colors: string[] = ((): string[] => {
          const stringToColour = (str: string) => {
            let hash = 0;
            str.split('').forEach(char => {
              hash = char.charCodeAt(0) + ((hash << 5) - hash)
            })
            let colour = '#'
            for (let i = 0; i < 3; i++) {
              const value = (hash >> (i * 8)) & 0xff
              colour += value.toString(16).padStart(2, '0')
            }
            return colour
          }

          const rgbValues: string[] = []

          for (const label of labels) {
            if (label.length < 6) {
              rgbValues.push(stringToColour(label + label))
              console.log(label + ": " + stringToColour(label))
            } else {
              rgbValues.push(stringToColour(label))
            }
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
