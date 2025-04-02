import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
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
      path: '/election/:electionId/party/:partyName',
      name: 'party',
      component: PartyView,
    },

  ],
})

export default router
