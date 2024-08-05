<template>
  <v-container class="transparent-bg pa-0">
    <v-sparkline
      v-if="contributions.length"
      :auto-line-width="autoLineWidth"
      :fill="fill"
      :gradient="isDark ? darkGradient : lightGradient"
      :gradient-direction="gradientDirection"
      :line-width="width"
      :model-value="contributions"
      :padding="padding"
      :smooth="smooth"
      :stroke-linecap="lineCap"
      :type="type"
      auto-draw
      class="transparent-bg"
      height="50"
    ></v-sparkline>
    <div class="text-caption text-center mt-1">
      <v-icon small :color="isDark ? 'grey-lighten-2' : 'primary'">mdi-source-branch</v-icon>
      <span class="ml-1">Repository Activity</span>
      <strong class="ml-2">Total Commits: {{ totalContributions }}</strong>
    </div>
  </v-container>
</template>

<script>
import axios from 'axios';
import { useData } from 'vitepress';
import { computed } from 'vue';

export default {
  setup() {
    const { isDark } = useData();
    return { isDark };
  },
  data: () => ({
    width: 1,
    smooth: true,
    padding: 4,
    lineCap: 'round',
    lightGradient: ['#42b3f4', '#00c6ff', '#0099ff'],
    darkGradient: ['#8E24AA', '#AB47BC', '#CE93D8'],
    contributions: [],
    gradientDirection: 'top',
    type: 'trend',
    autoLineWidth: true,
    fill: false,
  }),
  computed: {
    totalContributions() {
      return this.contributions.reduce((sum, value) => sum + value, 0);
    }
  },
  mounted() {
    this.fetchContributions();
  },
  methods: {
    async fetchContributions() {
      try {
        const owner = 'M1hono';
        const repo = 'CrychicDoc';
        let allCommits = [];
        let page = 1;
        
        while (true) {
          const response = await axios.get(`https://api.github.com/repos/${owner}/${repo}/commits`, {
            params: {
              page: page,
              per_page: 100  // Get 100 records per page
            }
          });
          
          if (response.data.length === 0) break;  // Exit loop if no more commits
          
          allCommits = allCommits.concat(response.data);
          page++;
        }
        
        this.contributions = this.processContributions(allCommits);
      } catch (error) {
        console.error('Error fetching GitHub commit data:', error);
      }
    },
    processContributions(commits) {
      const contributionsMap = {};
      const today = new Date();
      const thirtyDaysAgo = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
      
      commits.forEach(commit => {
        const commitDate = new Date(commit.commit.author.date);
        if (commitDate >= thirtyDaysAgo) {
          const dateString = commitDate.toISOString().split('T')[0];
          contributionsMap[dateString] = (contributionsMap[dateString] || 0) + 1;
        }
      });
      const contributions = Array(30).fill(0);
      for (let i = 0; i < 30; i++) {
        const date = new Date(today.getTime() - i * 24 * 60 * 60 * 1000);
        const dateString = date.toISOString().split('T')[0];
        contributions[29 - i] = contributionsMap[dateString] || 0;
      }
      return contributions;
    },
  },
}
</script>

<style scoped>
.transparent-bg {
  background-color: transparent !important;
}
</style>