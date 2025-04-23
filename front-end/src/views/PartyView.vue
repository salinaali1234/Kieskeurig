<script setup lang="ts">
import { ref, onMounted, type Ref } from 'vue';
import { useRoute } from 'vue-router';
import { type Candidate } from '@/models/Candidate';

let candidates: Ref<Candidate[]> = ref<Candidate[]>([])
const route = useRoute()
const electionId: string | string[] = route.params.electionId
const partyName: string | string[] = route.params.partyName
const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL
const url: string = `${VITE_APP_BACKEND_URL}/api/candidates/${electionId}/${partyName}`

onMounted(async () => {
  try {
    const repsonse = await fetch(url)
    candidates.value = await repsonse.json()
  } catch (error) {
    console.log(error)
  }
})

</script>

<template>
    <!-- Renders the party name after the candidates have been fetched -->
    <h1 v-if="candidates.length > 0">{{candidates[0].registeredName}}</h1>
    <hr>
    <br>

    <h1>Kandidaten</h1>

  <table>
    <tr>
      <th>ID</th>
      <th>Naam</th>
    </tr>
    <tr v-for="candidate in candidates" :key="candidate.candidateIdentifier">
      <td>{{candidate.candidateIdentifier}}</td>
      <td>{{candidate.firstName}} {{candidate.namePrefix}} {{candidate.lastName}}</td>
    </tr>
  </table>
</template>