<script setup lang="ts">
import {onMounted, ref} from 'vue';
import sessionService from '@/service/session-singleton';

const menuOpen = ref(false);
const statsDropdownOpen = ref(false);
const user = ref(sessionService.currentAccount); // gebruiker ophalen

function closeMenus() {
  menuOpen.value = false;
  statsDropdownOpen.value = false;
}

function logout() {
  sessionService.logout();
  window.location.reload(); // Refresh de pagina volledig
}

const navLinks = ref([
  { name: 'Partijen', to: '/parties', class: 'nav-link' },
  { name: 'Vergelijken', to: '/vergelijken', class: 'nav-link' },
]);

onMounted(() => {
  document.addEventListener("click", (e) => {
    const header = document.querySelector(".header");
    if (header && !header.contains(e.target as Node)) {
      closeMenus();
    }
  });
});


</script>

<template>
  <header class="header">
    <div class="container">
      <RouterLink to="/" class="title">
        Kies<span class="highlight">Keurig</span>
      </RouterLink>

      <button class="menu-btn" @click="menuOpen = !menuOpen">☰</button>

      <nav :class="{ 'open': menuOpen }">
        <div class="dropdown">
          <button class="dropdown-toggle" @click.stop="statsDropdownOpen = !statsDropdownOpen">
            Statistieken
            <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" width="16" height="16">
              <path d="M6 9l6 6 6-6" />
            </svg>
          </button>
          <ul v-if="statsDropdownOpen" class="dropdown-menu">
            <li><RouterLink to="/national" @click="closeMenus">Nationaal</RouterLink></li>
            <li><RouterLink to="/provinces" @click="closeMenus">Provincies</RouterLink></li>
            <li><RouterLink to="/constituencies" @click="closeMenus">Kieskringen</RouterLink></li>
          </ul>
        </div>

        <RouterLink
          v-for="link in navLinks"
          :key="link.to"
          :to="link.to"
          :class="link.class"
          @click="menuOpen = false"
        >
          {{ link.name }}
        </RouterLink>

        <!-- ✅ Ingelogd -->
        <div v-if="user" class="user-info">
           {{ user.name }}
          <button class="btn" @click="logout">Uitloggen</button>
        </div>

        <!-- ❌ Niet ingelogd -->
        <div v-else class="auth-buttons">
          <RouterLink to="/login" class="btn" @click="closeMenus">Inloggen</RouterLink>
          <RouterLink to="/register" class="btn" @click="closeMenus">Registreren</RouterLink>
        </div>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: 1rem;
}
button {
  all: unset;
  cursor: pointer;
  font-family: inherit;
}

.nav-link, .btn {
  padding-left: 1rem;
  padding-right: 1rem;
}

.header {
  width: 100%;
  background-color: var(--primary-clr);
  padding: 1rem 0;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1000;
}

.container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 90%;
  margin: 0 auto;
}

.title {
  font-weight: bold;
  font-size: 2rem;
  color: var(--text-clr);
  text-decoration: none;
}

.highlight {
  color: var(--accent-clr);
}


.menu-btn {
  display: none;
  font-size: 2rem;
  background: none;
  border: none;
  color: var(--accent-clr);
  cursor: pointer;
}


nav {
  display: flex;
  gap: 1rem;
  align-items: center;
  z-index: 999;
}

.nav-link {
  background-color: #e0c3f7;
  color: black;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  font-size: 1rem;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  gap: 0.4rem;
}


.nav-link:hover {
  background-color: #d4b6f0;
}


.btn {
  background-color: var(--accent-clr);
  color: black;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: bold;
  text-decoration: none;
  transition: 0.3s;
}

.btn:hover {
  background-color: #e6b800;
}


.dropdown {
  position: relative;
}

.icon {
  transition: transform 0.3s;
  fill: currentColor;
}

.dropdown-toggle.open .icon {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: var(--primary-clr);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  min-width: 160px;
  z-index: 1000;
}

.dropdown-menu li {
  list-style: none;
}

.dropdown-menu a {
  display: block;
  padding: 0.5rem 1rem;
  color: var(--secondary-clr);
  text-decoration: none;
  transition: background-color 0.3s;
}

.dropdown-menu a:hover {
  background-color: var(--accent-clr);
  color: black;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 1rem; /* of 10px als je dat fijner vindt */
}

@media (max-width: 768px) {
  .menu-btn {
    display: block;
  }

  nav {
    position: absolute;
    top: 70px;
    right: 0;
    background: var(--primary-clr);
    flex-direction: column;
    gap: 1rem;
    width: 100%;
    text-align: center;
    padding: 1rem 0;
    display: none;
  }

  nav.open {
    display: flex;
    flex-direction: column;
    align-items: stretch;
    max-height: 90vh;
    overflow-y: auto;
  }

  .nav-link, .btn {
    width: 100%;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    box-sizing: border-box;
  }

  .dropdown-menu {
    position: absolute;
    top: 100%;
    left: 40%;
    background-color: var(--primary-clr);
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    min-width: 60px;
    z-index: 1000;
  }
}
</style>
