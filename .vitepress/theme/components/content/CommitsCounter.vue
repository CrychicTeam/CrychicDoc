<template>
    <div class="commits-counter" :class="{ 'dark-mode': isDark }">
        <v-chart
            v-if="contributions.length"
            :option="chartOptions"
            :autoresize="true"
            class="sparkline-chart"
        />
        <div class="text-caption text-center mt-1">
            <svg
                class="theme-icon"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="currentColor"
            >
                <path
                    d="M17,17H22L19,14L22,11H17V13H14.5C11.84,13 10.22,13.65 10.22,13.65V11.88C10.22,11.88 11.84,11.22 14.5,11.22H17V13H22L19,16L22,19H17V17Z"
                />
            </svg>
            <span class="ml-1">{{ texts.repoActivity }}</span>
            <strong class="ml-2"
                >{{ texts.recentCommits }} {{ totalContributions }}</strong
            >
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { ref, computed, onMounted } from "vue";
    import { useData } from "vitepress";
    import VChart from "vue-echarts";
    import { use } from "echarts/core";
    import { LineChart } from "echarts/charts";
    import { TooltipComponent, GridComponent } from "echarts/components";
    import { CanvasRenderer } from "echarts/renderers";
    import utils from "@utils";

    use([LineChart, TooltipComponent, GridComponent, CanvasRenderer]);

    const props = defineProps({
        username: {
            type: String,
            default: "PickAID",
        },
        repoName: {
            type: String,
            default: "CrychicDoc",
        },
        daysToFetch: {
            type: Number,
            default: 30,
        },
        height: {
            type: Number,
            default: 50,
        },
        lineWidth: {
            type: Number,
            default: 2,
        },
        fill: {
            type: Boolean,
            default: true,
        },
        smooth: {
            type: Boolean,
            default: true,
        },
    });

    const { isDark, lang } = useData();

    const contributions = ref<number[]>([]);

    const totalContributions = computed(() =>
        utils.charts.github.commitProcessor.getTotalContributions(
            contributions.value
        )
    );

    const texts = computed(() => {
        return {
            repoActivity: utils.charts.github.getGithubText(
                "repoActivity",
                lang.value
            ),
            recentCommits: utils.charts.github.getGithubText(
                "recentCommits",
                lang.value
            ),
        };
    });

    /**
     * Generate chart options using extracted chart utilities
     */
    const chartOptions = computed(() =>
        utils.charts.github.chartOptions.generateSparklineOptions(
            contributions.value,
            isDark.value,
            {
                smooth: props.smooth,
                lineWidth: props.lineWidth,
                fill: props.fill,
            }
        )
    );

    /**
     * Fetch commit data using extracted GitHub utilities
     */
    const fetchContributions = async () => {
        try {
            const commits = await utils.charts.github.githubApi.fetchAllCommits(
                props.username,
                props.repoName
            );

            contributions.value =
                utils.charts.github.commitProcessor.processContributions(
                    commits,
                    props.daysToFetch
                );
        } catch (error) {
            console.error("Error fetching commit data:", error);
            contributions.value = [];
        }
    };

    onMounted(() => {
        fetchContributions();
    });
</script>

<style scoped>
    .commits-counter {
        background-color: transparent;
        padding: 0;
        color: var(--commits-text-color);
    }

    .sparkline-chart {
        width: 100%;
        height: 50px;
        background-color: transparent;
    }

    .text-caption {
        font-size: 0.75rem;
        line-height: 1rem;
    }

    .text-center {
        text-align: center;
    }

    .mt-1 {
        margin-top: 0.25rem;
    }

    .ml-1 {
        margin-left: 0.25rem;
    }

    .ml-2 {
        margin-left: 0.5rem;
    }

    .theme-icon {
        color: var(--commits-icon-color);
        vertical-align: text-bottom;
    }

    .dark-mode .theme-icon {
        color: var(--vp-c-text-1, #b0bec5);
    }

    .commits-counter {
        color: var(--vp-c-text-1);
    }

    .dark-mode {
        color: var(--vp-c-text-1);
    }
</style>
