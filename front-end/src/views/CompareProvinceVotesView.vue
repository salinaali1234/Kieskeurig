<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import "../assets/tableStyle.css";

interface Party {
    partyName: string;
    validVotes: number;
}

interface CombinedParty {
    name: string;
    votes1: number | string;
    votes2: number | string;
}

let electionId = ""
const years = ["2023", "2021"];
const selectedYearA = ref("2023");
const selectedYearB = ref("2021");

const votesA = ref<Party[]>([]);
const votesB = ref<Party[]>([]);
const provinces = ref<string[]>([]);
const combinedParties = ref<CombinedParty[]>([]);
const selectedProvince = ref("");

const VITE_APP_BACKEND_URL: string = import.meta.env.VITE_APP_BACKEND_URL;
let provinceUrl: string = `${VITE_APP_BACKEND_URL}/api/provinces`;

async function fetchVotes(year: string): Promise<Party[]> {
    electionId = "TK" + year;
    const response = await fetch(`${VITE_APP_BACKEND_URL}/api/party/${electionId}/${selectedProvince.value}`);
    return await response.json();
}

function combineParties(list1: Party[], list2: Party[]): CombinedParty[] {
    const combinedMap: Record<string, CombinedParty> = {};

    list1.forEach(p => {
    if (!p.partyName || typeof p.validVotes !== 'number') {
        console.warn('Invalid item in list1:', p);
        return;
    }
    const name = p.partyName.trim();
    combinedMap[name] = {
        name,
        votes1: p.validVotes,
        votes2: "-",
    };
    });

    list2.forEach(p => {
    if (!p.partyName || typeof p.validVotes !== 'number') {
        console.warn('Invalid item in list2:', p);
        return;
    }
    const name = p.partyName.trim();
    if (combinedMap[name]) {
        combinedMap[name].votes2 = p.validVotes;
    } else {
        combinedMap[name] = {
            name,
            votes1: "-",
            votes2: p.validVotes,
        };
    }
    });

    console.log("Combined map:", combinedMap);
    return Object.values(combinedMap);
}

async function loadVotes() {
    votesA.value = await fetchVotes(selectedYearA.value);
    votesB.value = await fetchVotes(selectedYearB.value);

    combinedParties.value = combineParties(votesA.value, votesB.value);
    console.log(combineParties(votesA.value, votesB.value));
}

onMounted(async () => {
    try {
        const response = await fetch(provinceUrl);

        if (response.ok) {
            provinces.value = await response.json();
        }
    } catch (error) {
        console.error("Er is een fout opgetreden bij het ophalen van de data", error);
    }
});

watch([selectedYearA, selectedYearB, selectedProvince], loadVotes);
</script>

<template>
    <div class="container">
        <h1 class="page-title">Vergelijk verkiezingsresultaten</h1>

        <div class="dropdown-wrapper">
            <select v-model="selectedProvince" class="dropdown">
            <option disabled value="">Selecteer provincie</option>
                <option
                    v-for="province in provinces"
                    :key="province"
                    :value="province"
                >
                    {{province}}
                </option>
            </select>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon" aria-hidden="true">
                <path d="m6 9 6 6 6-6"></path>
            </svg>
        </div>

        <div class="dropdowns">
            <div>
                <label>Jaar A</label>
                <div class="dropdown-wrapper small-dropdown-wrapper">
                    <select v-model="selectedYearA" class="dropdown small-dropdown">
                        <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
                    </select>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon small-dropdown-icon" aria-hidden="true">
                        <path d="m6 9 6 6 6-6"></path>
                    </svg>
                </div>
            </div>

            <div>
            <label>Jaar B</label>
                <div class="dropdown-wrapper small-dropdown-wrapper">
                    <select v-model="selectedYearB" class="dropdown small-dropdown">
                        <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
                    </select>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-chevron-down h-4 w-4 opacity-50 dropdown-icon small-dropdown-icon" aria-hidden="true">
                        <path d="m6 9 6 6 6-6"></path>
                    </svg>
                </div>
            </div>
        </div>

        <div class="compare-table-wrapper">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Partij</th>
                        <th>{{ selectedYearA }}</th>
                        <th>{{ selectedYearB }}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="party in combinedParties" :key="party.name">
                        <td>{{ party.name }}</td>
                        <td>{{ party.votes1 }}</td>
                        <td>{{ party.votes2 }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<style scoped>
.page-title {
    text-align: center;
    font-size: 2rem;
    margin: 40px 0;
    font-weight: bold;
    color: white;
}

.dropdowns {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1rem;
    margin-bottom: 2rem;
}

label {
    display: block;
    margin-bottom: 4px;
    color: white;
}

.compare-table-wrapper {
    overflow-x: auto;
    padding: 0 1rem;
}

.small-dropdown {
    width: 120px;
}

.small-dropdown-icon {
    right: -12px;
}

.small-dropdown-wrapper {
    margin: 0;
}
</style>
