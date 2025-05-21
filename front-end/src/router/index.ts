// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PartiesInfoView from '../views/PartiesInfoView.vue'
import CandidatesView from '../views/CandidatesView.vue'
import PartyView from '@/views/PartyView.vue'
import VoteView from "@/views/VoteView.vue";
import Consistuency from "@/views/Consistuency.vue";
import CompareVotes from "@/views/CompareVotes.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/statistieken',
      name: 'Vote',
      component: VoteView,
    },
    {
      path:  `/Constituency/:constituencyId/:constituencyName`,
      name: `constituency`,
      component: Consistuency,
    },
    {
      path: '/election/:electionId/party/:partyName',
      name: 'party',
      component: PartyView,
    },
    {
      path: '/parties',
      name: 'parties',
      component: PartiesInfoView,
    },
    {
      path: '/parties/:partyId/candidates',
      name: 'candidates',
      component: CandidatesView,
      props: true
    },
    {
      path: '/vergelijken',
      name: 'Vergelijken',
      component: CompareVotes,
    },
  ],
})

export default router
