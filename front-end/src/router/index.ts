import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Constituencies from "@/views/Constituencies.vue";
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

    }
  ],
})

export default router
