// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import PartiesInfoView from '../views/PartiesInfoView.vue'
import CandidatesView from '../views/CandidatesView.vue'
import PartyView from '@/views/PartyView.vue'
import VoteView from "@/views/VoteView.vue";
import Consistuency from "@/views/Consistuency.vue";
import CompareVotes from "@/views/CompareVotes.vue";
import NationalVotes from "@/components/NationalVotes.vue";
import Constituencies from "@/components/Constituencies.vue";
import ProvinceVotes from "@/components/ProvinceVotes.vue";
import NetherlandsMap from "@/components/NetherlandsMap.vue";
import LoginView from "@/views/LoginView.vue"
import RegisterView from "@/views/RegisterView.vue"
import AdminView from "@/views/AdminView.vue"
import { SessionService } from '@/service/session-service.ts'
import sessionService from '@/service/session-singleton.ts'

const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
new SessionService(`${BASE_URL}/authentication`, 'session_jwt')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: NetherlandsMap,
      meta: { requiresAuth: false }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { requiresAuth: false }
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: { requiresAuth: false, requiresAdmin: false }
    },
    {
      path: '/statistieken',
      name: 'Vote',
      component: VoteView,
      meta: { requiresAuth: false }
    },
    {
      path:  `/Constituency/:constituencyId/:constituencyName`,
      name: `constituency`,
      component: Consistuency,
     meta: { requiresAuth: false }
    },
    {
      path: '/election/:electionId/party/:partyName',
      name: 'party',
      component: PartyView,
     meta: { requiresAuth: false }
    },
    {
      path: '/parties',
      name: 'parties',
      component: PartiesInfoView,
      meta: { requiresAuth: false }
    },
    {
      path: '/parties/:partyId/candidates',
      name: 'candidates',
      component: CandidatesView,
      props: true,
      meta: { requiresAuth: false }
    },
    {
      path: '/vergelijken',
      name: 'Vergelijken',
      component: CompareVotes,
      meta: { requiresAuth: false }
    },
    {
      path: '/national',
      name: 'national',
      component: NationalVotes,
    },
    {
      path: '/constituencies',
      name: 'constituencies',
      component: Constituencies,
    },
    {
      path: '/provinces',
      name: 'provinces',
      component: ProvinceVotes,
    },

  ]

})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !sessionService.isAuthenticated()) {
    next('/login')
  } else if (to.meta.requiresAdmin && !sessionService.isAdmin()) {
    next('/')
  } else {
    next()
  }
})

export default router;
