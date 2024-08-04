<template>
  <v-container class="transparent-bg pa-0">
    <v-sparkline
      v-if="contributions.length"
      :auto-line-width="autoLineWidth"
      :fill="fill"
      :gradient="gradient"
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
      <v-icon small color="primary">mdi-source-branch</v-icon>
      <span class="ml-1">最近活动</span>
      <strong class="ml-2">总提交数：{{ totalContributions }}</strong>
    </div>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data: () => ({
    width: 2,
    smooth: true,
    padding: 4,
    lineCap: 'round',
    gradient: ['#42b3f4', '#00c6ff', '#0099ff'],
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
        const response = await axios.get(`https://api.github.com/repos/${owner}/${repo}/commits`);
        this.contributions = this.processContributions(response.data);
      } catch (error) {
        console.error('获取GitHub提交数据时出错:', error);
      }
    },
    processContributions(commits) {
      const contributions = Array(30).fill(0);
      const today = new Date();
      
      commits.forEach(commit => {
        const commitDate = new Date(commit.commit.author.date);
        const diffDays = Math.floor((today - commitDate) / (1000 * 60 * 60 * 24));
        if (diffDays < 30) {
          contributions[29 - diffDays] += 1;
        }
      });
      
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