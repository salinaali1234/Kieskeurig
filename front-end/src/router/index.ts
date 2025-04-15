// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PartiesInfoView from '../views/PartiesInfoView.vue'
import Constituencies from "@/views/Constituencies.vue";
import VoteView from "@/views/VoteView.vue";
import CandidatesView from '../views/CandidatesView.vue'
// import StatisticsView from '../views/StatisticsView.vue'
// import VotingGuideView from '../views/VotingGuideView.vue'
// import RegisterView from '../views/RegisterView.vue'
import PartyView from '@/views/PartyView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },

    {
      path: '/kieskringen',
      name: 'kieskringen',
      component:Constituencies,

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
      component: PartiesInfoView,
    },
    {
      path: '/parties/:partyId/candidates',
      name: 'candidates',
      component: CandidatesView,
      props: true
    },
    // {
    //   path: '/statistieken',
    //   name: 'statistics',
    //   component: StatisticsView
    // },
    // {
    //   path: '/stemwijzer',
    //   name: 'voting-guide',
    //   component: VotingGuideView
    // },
    // {
    //   path: '/register',
    //   name: 'register',
    //   component: RegisterView
    // }
  ],
})

export default router
