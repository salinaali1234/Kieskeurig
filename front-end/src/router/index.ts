import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Constituencies from "@/views/Constituencies.vue";
import PartiesView from '../views/PartiesView.vue'
import CandidatesView from '../views/CandidatesView.vue'
import PartyView from '@/views/PartyView.vue'
import VoteView from "@/views/VoteView.vue";

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
      path: '/election/:electionId/party/:partyName',
      name: 'party',
      component: PartyView,
    },
    {
      path: '/parties',
      name: 'parties',
      component: PartiesView,
    },
    {
      path: '/parties/:partyId/candidates',
      name: 'candidates',
      component: CandidatesView,
      props: true
    },
  ],
})

export default router
