<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js';
import { Bar } from 'vue-chartjs';

const VITE_APP_BACKEND_URL = import.meta.env.VITE_APP_BACKEND_URL;

interface PartyWithInfo { partyId: number; partyName: string; seats: number; }
interface Candidate    { id: number; firstName: string; lastName: string; gender: string; localityName: string; }

const partiesList       = ref<PartyWithInfo[]>([]);
const loading           = ref(true);
const error             = ref<string | null>(null);
const sortOrder         = ref<'seats-desc' | 'seats-asc' | 'alphabetical'>('seats-desc');
const currentPartyId    = ref<number | null>(null);
const currentPartyName  = ref('');
const currentCandidates = ref<Candidate[]>([]);
const candidatesLoading = ref(false);


const sortOptions = [
  { value: 'seats-desc', label: 'Zetels (hoog → laag)' },
  { value: 'seats-asc',  label: 'Zetels (laag → hoog)' },
  { value: 'alphabetical', label: 'Alfabetisch (A → Z)' }
];

async function fetchParties() {
  loading.value = true;
  error.value = null;
  try {
    const res = await fetch(`${VITE_APP_BACKEND_URL}/api/parties?sort=${sortOrder.value}`);
    if (!res.ok) throw new Error('Kon partijen niet laden');
    const data: PartyWithInfo[] = await res.json();


    const dummyParties = data.map(p => ({
      ...p,
      seats: Math.floor(Math.random() * 31) + 5 // zetels van 5 t/m 35
    }));

    partiesList.value = dummyParties;

  } catch (e) {
    error.value = e instanceof Error ? e.message : String(e);
  } finally {
    loading.value = false;
  }
}

async function showCandidates(id: number, name: string) {
  candidatesLoading.value = true;
  currentPartyId.value   = id;
  currentPartyName.value = name;
  try {
    const res = await fetch(`${VITE_APP_BACKEND_URL}/api/candidates/party/${id}`);
    if (!res.ok) throw new Error('Kon kandidaten niet laden');
    currentCandidates.value = await res.json();
  } catch (e) { error.value = e instanceof Error ? e.message : String(e); }
  finally { candidatesLoading.value = false; }
}

function resetView() {
  currentPartyId.value = null;
  currentCandidates.value = [];
}

watch(sortOrder, fetchParties);
onMounted(fetchParties);

// ——— BarChart setup ———
ChartJS.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend);
const labels    = ref<string[]>([]);
const seatsData = ref<number[]>([]);

watch(partiesList, list => {
  labels.value = list.map(p => p.partyName);
  seatsData.value = list.map(p => p.seats);
}, { immediate: true });

const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false },
    title: { display: true, text: 'Zetels per partij' },
    // custom plugin om de achtergrond wit te vullen
    beforeDraw: {
      id: 'whiteBackground',
      beforeDraw: (chart) => {
        const ctx = chart.ctx;
        ctx.save();
        ctx.fillStyle = 'white';
        ctx.fillRect(0, 0, chart.width, chart.height);
        ctx.restore();
      }
    }
  },
  scales: {
    y: { beginAtZero: true, ticks: { stepSize: 1 } }
  }
};
const options = {
  responsive: true,
  plugins: {
    legend: { display: false },
    title: {
      display: true,
      text: 'Zetels per partij'
    },

    whiteBackground: {
      beforeDraw: (chart: never) => {
        const { ctx, width, height } = chart;
        ctx.save();
        ctx.fillStyle = 'white';
        ctx.fillRect(0, 0, width, height);
        ctx.restore();
      }
    }
  },
  scales: {
    y: { beginAtZero: true, ticks: { stepSize: 1 } }
  }
}

// registreer de plugin
ChartJS.register({
  id: 'whiteBackground',
  beforeDraw: (chart) => options.plugins!.whiteBackground.beforeDraw(chart)
});
</script>

<template>
  <div class="parties-container">
    <!-- Bar chart -->
    <Bar
      :data="{ labels: labels, datasets: [{ label: 'Zetels', data: seatsData, backgroundColor: '#3E2858' }] }"
      :options="chartOptions"
      class="chart"
    />

    <!-- Header + sort -->
    <div class="header-container">
      <h1>Verkozen zetels per partij</h1>
      <div class="sort-container" v-if="!currentPartyId">
        <label for="sort-select">Sorteer op:</label>
        <select id="sort-select" v-model="sortOrder" class="sort-select">
          <option v-for="opt in sortOptions" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
        </select>
      </div>
    </div>

    <!-- Loading / error -->
    <div v-if="loading" class="loading">Laden...</div>
    <div v-else-if="error" class="error">
      {{ error }} <button @click="fetchParties" class="retry-btn">Opnieuw</button>
    </div>

    <!-- Partijen of kandidaten -->
    <div v-if="!currentPartyId" class="party-grid">
      <div v-for="p in partiesList" :key="p.partyId" class="party-card" @click="showCandidates(p.partyId, p.partyName)">
        <h2>{{ p.partyName }}</h2>
        <p><strong>Zetels:</strong> {{ p.seats }}</p>
      </div>
    </div>
    <div v-else>
      <button @click="resetView" class="back-btn">← Terug naar partijen</button>
      <h1 class="subheader">Kandidaten van {{ currentPartyName }}</h1>
      <div v-if="candidatesLoading" class="loading">Kandidaten laden...</div>
      <div v-else-if="currentCandidates.length === 0" class="empty">Geen kandidaten gevonden</div>
      <div class="candidates-list">
        <div v-for="c in currentCandidates" :key="c.id" class="candidate-item">
          <h3>{{ c.firstName }} {{ c.lastName }}</h3>
          <p>Geslacht: {{ c.gender }}</p>
          <p>Locatie: {{ c.localityName }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.parties-container { padding: 2rem; max-width: 900px; margin: 0 auto; }
.chart { margin-bottom: 2rem; }

/* Header */
.header-container
{ display: flex;
  justify-content: space-between;
  align-items: center;
  background: #3E2858;
  padding: 1rem;
  border-radius: 8px; }

.header-container h1
{ color: white;
  margin:0;
}

.sort-container label
{ color: white;
  margin-right: .5rem;
}

.sort-select {
  background: #d1a5e6;
  color: #2c3e50;
  border: 1px solid #d1a5e6;
  border-radius:4px;
  padding: .5rem 1rem;
}

/* Grid */
.party-grid
{ display:grid;
  grid-template-columns: repeat(auto-fill,minmax(280px,1fr));
  gap:1.5rem;
}
.party-card, .candidate-item {
  background:white;
  color:black;
  padding:1.5rem;
  border-radius:10px;
  box-shadow:0 2px 6px rgba(0,0,0,0.1);
  transition: transform .2s;
}
.party-card:hover
{ transform: translateY(-3px);}

.candidate-item:hover
{ transform: translateY(-2px); }

/* Back button & subheader */
.back-btn {
  background:#DEBFE9;
  color:#000; border:none;
  padding:.5rem 1rem;
  border-radius:6px;
  cursor:pointer;
  margin-bottom:1rem;
}
.subheader {
  color: white;
  background:#3E2858;
  padding:.5rem 1rem;
  border-radius:6px; }

/* States */
.loading {
  text-align:center;
  padding:2rem;
  font-style:italic;
  color:#666; }
.error {
  background:#ffebee;
  color:#d32f2f;
  padding:1rem;
  border-radius:4px;
  margin:1rem 0;
  display:flex; justify-content:space-between; align-items:center; }
.retry-btn
{ background:#1976d2;
  color:white;
  border:none;
  padding:.5rem 1rem;
  border-radius:4px;
  cursor:pointer; }

.retry-btn:hover
{ background:#1565c0; }

/* Candidates list */
.candidates-list { display:grid;
  gap:1.5rem;
  margin-top:1rem; }
</style>
