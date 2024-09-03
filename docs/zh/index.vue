<template>
  <v-container class="transparent-bg pa-0">
    <v-sparkline
      v-if="contributions.length"
      :auto-line-width="autoLineWidth"
      :fill="fill"
      :gradient="gradientColors"
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
      <v-icon small class="theme-icon">mdi-source-branch</v-icon>
      <span class="ml-1">仓库活动</span>
      <strong class="ml-2">最近提交数：{{ totalContributions }}</strong>
    </div>
  </v-container>
</template>

<script>
import axios from 'axios';
import { useData } from 'vitepress';
import { computed, onMounted, ref, watch } from 'vue';

export default {
  setup() {
    const { isDark } = useData();
    const contributions = ref([]);

    const totalContributions = computed(() => {
      return contributions.value.reduce((sum, value) => sum + value, 0);
    });

    const gradientColors = computed(() => {
      return isDark.value 
        ? ['#4A148C', '#6A1B9A', '#8E24AA'] // Updated dark mode colors
        : ['#1565C0', '#1976D2', '#2196F3']; // Updated light mode colors
    });

    const fetchContributions = async () => {
      try {
        const owner = 'CrychicTeam';
        const repo = 'CrychicDoc';
        let allCommits = [];
        let page = 1;
        
        while (true) {
          const response = await axios.get(`https://api.github.com/repos/${owner}/${repo}/commits`, {
            params: {
              page: page,
              per_page: 100
            }
          });
          
          if (response.data.length === 0) break;
          
          allCommits = allCommits.concat(response.data);
          page++;
        }
        
        contributions.value = processContributions(allCommits);
      } catch (error) {
        console.error('获取GitHub提交数据时出错:', error);
      }
    };

    const processContributions = (commits) => {
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
    };

    onMounted(() => {
      fetchContributions();
    });

    return {
      contributions,
      totalContributions,
      gradientColors,
      isDark,
      width: 1,
      smooth: true,
      padding: 4,
      lineCap: 'round',
      gradientDirection: 'top',
      type: 'trend',
      autoLineWidth: true,
      fill: false,
    };
  },
};
</script>

<style scoped>
.transparent-bg {
  background-color: transparent !important;
}

.theme-icon {
  color: var(--vp-c-brand);
}

:root {
  --vp-c-brand: #1976D2;
}

.dark .theme-icon {
  color: var(--vp-c-text-1);
}

.dark {
  --vp-c-text-1: #B0BEC5;
}
</style>