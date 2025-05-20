<script setup lang="ts">
import { ref } from 'vue';

const menuOpen = ref(false);
const statsDropdownOpen = ref(false);

function closeMenus() {
  menuOpen.value = false;
  statsDropdownOpen.value = false;
}

</script>

<template>
  <header class="header">
    <div class="container">
      <RouterLink to="/" class="title">
        Kies<span class="highlight">Keurig</span>
      </RouterLink>


      <button class="menu-btn" @click="menuOpen = !menuOpen">
        ☰
      </button>


      <nav :class="{ 'open':menuOpen }">
        <div class="dropdown" @mouseleave="statsDropdownOpen = false">
          <button class="dropdown-toggle" @click="statsDropdownOpen = !statsDropdownOpen">
            Statistieken
            <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6"/></svg>
          </button>
          <ul v-if="statsDropdownOpen" class="dropdown-menu">
            <li><RouterLink to="/national" @click="closeMenus">Nationaal</RouterLink></li>
            <li><RouterLink to="/provinces" @click="closeMenus">Provincies</RouterLink></li>
            <li><RouterLink to="/constituencies" @click="closeMenus">Kieskringen</RouterLink></li>
          </ul>
        </div>
        <RouterLink to="/parties" @click="menuOpen = false">Partijen</RouterLink>
        <RouterLink to="/vergelijken" @click="menuOpen = false">Vergelijken</RouterLink>
        <RouterLink to="/register" class="btn" @click="menuOpen = false">Registreren</RouterLink>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.header {
  width: 100%;
  background-color: var(--primary-clr);
  padding: 1rem 0;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
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


nav {
  display: flex;
  gap: 1.5rem;
}


.menu-btn {
  display: none;
  font-size: 2rem;
  background: none;
  border: none;
  color: var(--accent-clr);
  cursor: pointer;
}


nav a {
  color: var(--secondary-clr);
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

nav a:hover {
  color: var(--accent-clr);
}


.btn {
  background-color: var(--accent-clr);
  color: black;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  text-decoration: none;
  font-weight: bold;
  transition: 0.3s;
}

.btn:hover {
  background-color: #e6b800;
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
  }
}
</style>






