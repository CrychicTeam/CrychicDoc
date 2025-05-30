/**
 * GitHub integration utilities for charts
 * Extracted from CommitsCounter component
 */

import type { GitHubCommit, TranslationDictionary } from '../types';

/**
 * GitHub API utilities
 */
export const githubApi = {
  /** Fetch all commits from a GitHub repository */
  fetchAllCommits: async (username: string, repoName: string): Promise<GitHubCommit[]> => {
    let allCommits: GitHubCommit[] = [];
    let page = 1;

    while (true) {
      try {
        const response = await fetch(
          `https://api.github.com/repos/${username}/${repoName}/commits?page=${page}&per_page=100`
        );
        
        if (!response.ok) {
          throw new Error(`GitHub API error: ${response.status}`);
        }
        
        const commits: GitHubCommit[] = await response.json();
        
        if (commits.length === 0) break;
        
        allCommits = allCommits.concat(commits);
        page++;
      } catch (error) {
        console.error("Error fetching GitHub commit data:", error);
        break;
      }
    }

    return allCommits;
  }
};

/**
 * Commit data processing utilities
 */
export const commitProcessor = {
  /** Process commits into daily contribution counts */
  processContributions: (commits: GitHubCommit[], daysToFetch: number = 30): number[] => {
    const contributionsMap: Record<string, number> = {};
    const today = new Date();
    const daysAgo = new Date(today.getTime() - daysToFetch * 24 * 60 * 60 * 1000);

    commits.forEach((commit) => {
      const commitDate = new Date(commit.commit.author.date);
      if (commitDate >= daysAgo) {
        const dateString = commitDate.toISOString().split("T")[0];
        contributionsMap[dateString] = (contributionsMap[dateString] || 0) + 1;
      }
    });

    const contributions = Array(daysToFetch).fill(0);
    for (let i = 0; i < daysToFetch; i++) {
      const date = new Date(today.getTime() - i * 24 * 60 * 60 * 1000);
      const dateString = date.toISOString().split("T")[0];
      contributions[daysToFetch - 1 - i] = contributionsMap[dateString] || 0;
    }
    
    return contributions;
  },

  /** Calculate total contributions */
  getTotalContributions: (contributions: number[]): number => {
    return contributions.reduce((sum, value) => sum + value, 0);
  }
};

/**
 * Chart options generator for commit sparklines
 */
export const chartOptions = {
  /** Generate ECharts options for commit sparkline */
  generateSparklineOptions: (
    contributions: number[], 
    isDark: boolean = false,
    options: {
      smooth?: boolean;
      lineWidth?: number;
      fill?: boolean;
    } = {}
  ) => {
    const { smooth = true, lineWidth = 2, fill = true } = options;
    
    const gradientColors = isDark
      ? ["#4A148C", "#6A1B9A", "#8E24AA"]
      : ["#1565C0", "#1976D2", "#2196F3"];

    return {
      grid: {
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
      },
      xAxis: {
        type: 'category',
        show: false,
        boundaryGap: false,
        data: Array.from({ length: contributions.length }, (_, i) => i)
      },
      yAxis: {
        type: 'value',
        show: false,
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        },
        formatter: (params: any) => {
          const dataIndex = params[0].dataIndex;
          const date = new Date();
          date.setDate(date.getDate() - (contributions.length - 1 - dataIndex));
          return `${date.toLocaleDateString()}: ${params[0].value} commits`;
        }
      },
      series: [
        {
          type: 'line',
          data: contributions,
          showSymbol: false,
          smooth,
          lineStyle: {
            width: lineWidth,
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 1,
              y2: 0,
              colorStops: gradientColors.map((color, index) => ({
                offset: index / (gradientColors.length - 1),
                color: color
              }))
            }
          },
          areaStyle: fill ? {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: gradientColors[0] + '80'
                },
                {
                  offset: 1,
                  color: gradientColors[gradientColors.length - 1] + '20'
                }
              ]
            }
          } : undefined
        }
      ]
    };
  }
};

/**
 * GitHub chart translations
 */
export const githubTranslations: TranslationDictionary = {
  repoActivity: {
    "en-US": "Repository Activity",
    "zh-CN": "仓库活动",
  },
  recentCommits: {
    "en-US": "Recent commits:",
    "zh-CN": "最近提交数：",
  },
};

/**
 * Get GitHub chart translation
 */
export const getGithubText = (key: string, lang: string): string => {
  return githubTranslations[key]?.[lang] || githubTranslations[key]?.["en-US"] || key;
}; 