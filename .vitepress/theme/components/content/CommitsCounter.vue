<template>
    <div class="commits-counter-container">
        <div class="commits-counter">
            <div class="chart-container">
                <div
                    class="chart-loading"
                    v-if="!chartOptions || !contributions.length"
                >
                    <div class="loading-bars">
                        <div class="bar" v-for="i in 20" :key="i"></div>
                    </div>
                </div>
                <ClientOnly v-else>
                    <v-chart
                        :option="chartOptions"
                        :autoresize="true"
                        class="main-chart"
                    />
                </ClientOnly>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { ref, computed, onMounted } from "vue";
    import { useData } from "vitepress";
    import { defineAsyncComponent } from "vue";
    import utils from "@utils";

    // Async import for vue-echarts to avoid SSR issues
    const VChart = defineAsyncComponent(async () => {
        const { default: VChart } = await import("vue-echarts");
        const { use } = await import("echarts/core");
        const { LineChart } = await import("echarts/charts");
        const { TooltipComponent, GridComponent } = await import(
            "echarts/components"
        );
        const { CanvasRenderer } = await import("echarts/renderers");

        use([LineChart, TooltipComponent, GridComponent, CanvasRenderer]);

        return VChart;
    });

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
            default: 120,
        },
        lineWidth: {
            type: Number,
            default: 4,
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
     * Generate enhanced chart options with beautiful styling
     */
    const chartOptions = computed(() => {
        if (!contributions.value.length) return {};

        const maxValue = Math.max(...contributions.value);

        return {
            grid: {
                left: 40,
                right: 40,
                top: 30,
                bottom: 30,
            },
            xAxis: {
                type: "category",
                data: contributions.value.map((_, index) =>
                    new Date(
                        Date.now() -
                            (contributions.value.length - 1 - index) *
                                24 *
                                60 *
                                60 *
                                1000
                    ).toLocaleDateString("zh-CN", {
                        month: "short",
                        day: "numeric",
                    })
                ),
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: isDark.value
                            ? "rgba(255, 255, 255, 0.1)"
                            : "rgba(0, 0, 0, 0.1)",
                    },
                },
                axisTick: { show: false },
                axisLabel: {
                    show: true,
                    color: isDark.value
                        ? "rgba(255, 255, 255, 0.6)"
                        : "rgba(0, 0, 0, 0.6)",
                    fontSize: 12,
                    interval: Math.floor(contributions.value.length / 6),
                },
                splitLine: { show: false },
            },
            yAxis: {
                type: "value",
                axisLine: { show: false },
                axisTick: { show: false },
                axisLabel: {
                    show: true,
                    color: isDark.value
                        ? "rgba(255, 255, 255, 0.6)"
                        : "rgba(0, 0, 0, 0.6)",
                    fontSize: 12,
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: isDark.value
                            ? "rgba(255, 255, 255, 0.05)"
                            : "rgba(0, 0, 0, 0.05)",
                        type: "dashed",
                    },
                },
            },
            series: [
                {
                    type: "line",
                    data: contributions.value,
                    lineStyle: {
                        width: props.lineWidth,
                        color: {
                            type: "linear",
                            x: 0,
                            y: 0,
                            x2: 1,
                            y2: 0,
                            colorStops: [
                                {
                                    offset: 0,
                                    color: isDark.value ? "#60a5fa" : "#3b82f6",
                                },
                                {
                                    offset: 0.5,
                                    color: isDark.value ? "#a78bfa" : "#8b5cf6",
                                },
                                {
                                    offset: 1,
                                    color: isDark.value ? "#f472b6" : "#ec4899",
                                },
                            ],
                        },
                        shadowColor: isDark.value
                            ? "rgba(96, 165, 250, 0.3)"
                            : "rgba(59, 130, 246, 0.3)",
                        shadowBlur: 10,
                        shadowOffsetY: 2,
                    },
                    areaStyle: props.fill
                        ? {
                              color: {
                                  type: "linear",
                                  x: 0,
                                  y: 0,
                                  x2: 0,
                                  y2: 1,
                                  colorStops: [
                                      {
                                          offset: 0,
                                          color: isDark.value
                                              ? "rgba(96, 165, 250, 0.4)"
                                              : "rgba(59, 130, 246, 0.3)",
                                      },
                                      {
                                          offset: 0.7,
                                          color: isDark.value
                                              ? "rgba(167, 139, 250, 0.2)"
                                              : "rgba(139, 92, 246, 0.15)",
                                      },
                                      {
                                          offset: 1,
                                          color: isDark.value
                                              ? "rgba(244, 114, 182, 0.1)"
                                              : "rgba(236, 72, 153, 0.05)",
                                      },
                                  ],
                              },
                          }
                        : undefined,
                    symbol: "circle",
                    symbolSize: 6,
                    showSymbol: false,
                    emphasis: {
                        focus: "series",
                        showSymbol: true,
                        symbolSize: 8,
                        lineStyle: {
                            width: props.lineWidth + 2,
                        },
                    },
                    smooth: props.smooth,
                },
            ],
            tooltip: {
                trigger: "axis",
                backgroundColor: isDark.value
                    ? "rgba(15, 15, 15, 0.95)"
                    : "rgba(255, 255, 255, 0.95)",
                borderColor: isDark.value
                    ? "rgba(255, 255, 255, 0.1)"
                    : "rgba(0, 0, 0, 0.1)",
                textStyle: {
                    color: isDark.value ? "#ffffff" : "#1f2937",
                    fontSize: 14,
                },
                padding: [12, 16],
                borderRadius: 8,
                boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                formatter: function (params: any) {
                    const point = params[0];
                    const date = point.name;
                    const value = point.value || 0;
                    return `<div style="font-weight: 600;">${date}</div><div style="margin-top: 4px; color: ${point.color};">${value} commits</div>`;
                },
            },
        };
    });

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
    .commits-counter-container {
        width: 100vw;
        margin-left: 50%;
        transform: translateX(-50%);
        padding: 0;
        /* Fixed background colors */
        background: #ffffff;
        position: relative;
        overflow: hidden;
    }

    .dark .commits-counter-container {
        background: #1b1b1f;
    }

    .commits-counter {
        max-width: 1800px; /* Made even wider */
        margin: 0 auto;
        padding: 60px 24px;
        background: transparent;
        border: none;
        border-radius: 0;
        position: relative;
    }

    @media (min-width: 640px) {
        .commits-counter {
            padding: 80px 48px;
        }
    }

    @media (min-width: 960px) {
        .commits-counter {
            padding: 100px 64px;
        }
    }

    .chart-container {
        background: var(--vp-c-bg-soft);
        border: 1px solid var(--vp-c-divider);
        border-radius: 16px;
        padding: 40px 32px;
        position: relative;
        overflow: hidden;
        height: 400px; /* Reduced from 500px */
        width: 100%; /* Full width */
        display: flex;
        align-items: center;
        justify-content: center;
    }

    @media (min-width: 640px) {
        .chart-container {
            padding: 50px 40px;
            height: 480px; /* Reduced from 600px */
        }
    }

    @media (min-width: 960px) {
        .chart-container {
            padding: 60px 50px;
            height: 550px; /* Reduced from 700px */
        }
    }

    @media (min-width: 1200px) {
        .chart-container {
            height: 600px; /* Reduced from 800px */
            padding: 70px 60px;
        }
    }

    .main-chart {
        width: 100%;
        height: 100%;
    }

    .chart-loading {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .loading-bars {
        display: flex;
        align-items: end;
        gap: 6px;
        height: 80px; /* Larger loading bars */
    }

    .bar {
        width: 10px; /* Wider bars */
        background: linear-gradient(
            0deg,
            var(--vp-c-brand-1) 0%,
            var(--vp-c-brand-3) 100%
        );
        border-radius: 5px;
        animation: loading-wave 1.5s ease-in-out infinite;
        animation-delay: calc(var(--i) * 0.1s);
    }

    .bar:nth-child(1) {
        --i: 0;
        height: 20%;
    }
    .bar:nth-child(2) {
        --i: 1;
        height: 40%;
    }
    .bar:nth-child(3) {
        --i: 2;
        height: 60%;
    }
    .bar:nth-child(4) {
        --i: 3;
        height: 80%;
    }
    .bar:nth-child(5) {
        --i: 4;
        height: 100%;
    }
    .bar:nth-child(6) {
        --i: 5;
        height: 70%;
    }
    .bar:nth-child(7) {
        --i: 6;
        height: 30%;
    }
    .bar:nth-child(8) {
        --i: 7;
        height: 50%;
    }
    .bar:nth-child(9) {
        --i: 8;
        height: 90%;
    }
    .bar:nth-child(10) {
        --i: 9;
        height: 40%;
    }
    .bar:nth-child(11) {
        --i: 10;
        height: 60%;
    }
    .bar:nth-child(12) {
        --i: 11;
        height: 35%;
    }
    .bar:nth-child(13) {
        --i: 12;
        height: 75%;
    }
    .bar:nth-child(14) {
        --i: 13;
        height: 85%;
    }
    .bar:nth-child(15) {
        --i: 14;
        height: 45%;
    }
    .bar:nth-child(16) {
        --i: 15;
        height: 65%;
    }
    .bar:nth-child(17) {
        --i: 16;
        height: 25%;
    }
    .bar:nth-child(18) {
        --i: 17;
        height: 55%;
    }
    .bar:nth-child(19) {
        --i: 18;
        height: 95%;
    }
    .bar:nth-child(20) {
        --i: 19;
        height: 35%;
    }

    @keyframes loading-wave {
        0%,
        100% {
            transform: scaleY(1);
            opacity: 0.7;
        }
        50% {
            transform: scaleY(1.5);
            opacity: 1;
        }
    }

    /* Mobile responsive adjustments */
    @media (max-width: 768px) {
        .commits-counter {
            padding: 40px 16px;
            max-width: 100%;
        }

        .chart-container {
            height: 350px;
            padding: 30px 20px;
        }
    }
</style>
