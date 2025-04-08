// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import VoteView from "@/views/VoteView.vue";
import PartiesView from '../views/PartiesView.vue'
import CandidatesView from '../views/CandidatesView.vue'
// import StatisticsView from '../views/StatisticsView.vue'
// import VotingGuideView from '../views/VotingGuideView.vue'
// import RegisterView from '../views/RegisterView.vue'

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
