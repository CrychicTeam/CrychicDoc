<script setup lang="ts">
    import { ref, computed, onMounted, watch } from "vue";
    import { useData } from "vitepress";

    const props = defineProps({
        mode: {
            type: String,
            default: "interactive",
            validator: (value) => ["interactive", "static"].includes(value),
        },
        incomingDamage: { type: Number, default: 201 },
        armorToughness: { type: Number, default: 150 },
        minDamage: { type: Number, default: 0 },
        maxDamage: { type: Number, default: 200 },
        maxArmorPoints: { type: Number, default: 50 },
        isJavaEdition: { type: Boolean, default: true },
        translations: {
            type: Object,
            default: () => ({
                "en-US": {
                    incomingDamage: "Incoming Damage",
                    armorToughness: "Armor Toughness",
                    minDamage: "Min Damage",
                    maxDamage: "Max Damage",
                    actualDamage: "Actual Damage",
                    damageReduction: "Damage Reduction (%)",
                    armorPoints: "Armor Points",
                    maxArmorPoints: "Max Armor Points",
                    javaEdition: "Java Edition",
                    bedrockEdition: "Bedrock Edition",
                },
                "zh-CN": {
                    incomingDamage: "初始伤害",
                    armorToughness: "护甲韧性",
                    minDamage: "最小伤害",
                    maxDamage: "最大伤害",
                    actualDamage: "实际伤害",
                    damageReduction: "伤害减免 (%)",
                    armorPoints: "护甲值",
                    maxArmorPoints: "最大护甲值",
                    javaEdition: "Java版",
                    bedrockEdition: "基岩版",
                },
            }),
        },
    });

    const { isDark, lang } = useData();

    const chartRef = ref(null);
    let chartInstance = null;

    const incomingDamageRef = ref(props.incomingDamage);
    const armorToughnessRef = ref(props.armorToughness);
    const minDamageRef = ref(props.minDamage);
    const maxDamageRef = ref(props.maxDamage);
    const maxArmorPointsRef = ref(props.maxArmorPoints);
    const isJavaEditionRef = ref(props.isJavaEdition);

    const debugInfo = ref("");

    const localText = computed(
        () => props.translations[lang.value] || props.translations["en-US"]
    );

    const calculateDamage = (armor, toughness, damage, isJava) => {
        if (isJava) {
            const defensePoints = Math.min(
                20,
                Math.max(armor / 5, armor - damage / (2 + toughness / 4))
            );
            return damage * (1 - defensePoints / 25);
        } else {
            return damage * (1 - Math.min(20, armor) * 0.04);
        }
    };

    const chartData = computed(() => {
        const labels = [];
        const actualDamage = [];
        const damageReduction = [];

        for (let armor = 0; armor <= maxArmorPointsRef.value; armor += 1) {
            labels.push(armor);
            const damage = calculateDamage(
                armor,
                armorToughnessRef.value,
                incomingDamageRef.value,
                isJavaEditionRef.value
            );
            const clampedDamage = Math.max(
                minDamageRef.value,
                Math.min(maxDamageRef.value, damage)
            );
            actualDamage.push(Number(clampedDamage.toFixed(2)));
            damageReduction.push(
                Number(
                    (
                        (1 - clampedDamage / incomingDamageRef.value) *
                        100
                    ).toFixed(2)
                )
            );
        }

        return {
            labels,
            datasets: [
                {
                    label: localText.value.actualDamage,
                    borderColor: isDark.value ? "#ff6384" : "#ff3860",
                    backgroundColor: isDark.value
                        ? "rgba(255, 99, 132, 0.2)"
                        : "rgba(255, 56, 96, 0.2)",
                    data: actualDamage,
                    yAxisID: "y",
                },
                {
                    label: localText.value.damageReduction,
                    borderColor: isDark.value ? "#36a2eb" : "#3273dc",
                    backgroundColor: isDark.value
                        ? "rgba(54, 162, 235, 0.2)"
                        : "rgba(50, 115, 220, 0.2)",
                    data: damageReduction,
                    yAxisID: "y1",
                },
            ],
        };
    });

    // 图表选项
    const chartOptions = computed(() => ({
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            x: {
                title: {
                    display: true,
                    text: localText.value.armorPoints,
                    color: isDark.value ? "#ffffff" : "#363636",
                },
                ticks: {
                    color: isDark.value ? "#ffffff" : "#363636",
                },
                grid: {
                    color: isDark.value
                        ? "rgba(255, 255, 255, 0.1)"
                        : "rgba(0, 0, 0, 0.1)",
                },
                max: maxArmorPointsRef.value,
            },
            y: {
                type: "linear",
                display: true,
                position: "left",
                title: {
                    display: true,
                    text: localText.value.actualDamage,
                    color: isDark.value ? "#ffffff" : "#363636",
                },
                ticks: {
                    color: isDark.value ? "#ffffff" : "#363636",
                },
                grid: {
                    color: isDark.value
                        ? "rgba(255, 255, 255, 0.1)"
                        : "rgba(0, 0, 0, 0.1)",
                },
            },
            y1: {
                type: "linear",
                display: true,
                position: "right",
                title: {
                    display: true,
                    text: localText.value.damageReduction,
                    color: isDark.value ? "#ffffff" : "#363636",
                },
                min: 0,
                max: 100,
                ticks: {
                    color: isDark.value ? "#ffffff" : "#363636",
                },
                grid: {
                    drawOnChartArea: false,
                    color: isDark.value
                        ? "rgba(255, 255, 255, 0.1)"
                        : "rgba(0, 0, 0, 0.1)",
                },
            },
        },
        plugins: {
            legend: {
                labels: {
                    color: isDark.value ? "#ffffff" : "#363636",
                },
            },
        },
    }));

    const updateChart = () => {
        if (chartInstance) {
            chartInstance.data = chartData.value;
            chartInstance.options = chartOptions.value;
            chartInstance.update();
        }
    };

    onMounted(() => {
        if (chartRef.value) {
            import("chart.js/auto").then((ChartModule) => {
                const Chart = ChartModule.default;
                const ctx = chartRef.value.getContext("2d");
                chartInstance = new Chart(ctx, {
                    type: "line",
                    data: chartData.value,
                    options: chartOptions.value,
                });
            });
        }
    });

    watch(
        [
            incomingDamageRef,
            armorToughnessRef,
            minDamageRef,
            maxDamageRef,
            maxArmorPointsRef,
            isJavaEditionRef,
            isDark,
        ],
        () => {
            updateChart();
        }
    );

    const handleInput = (event, key) => {
        if (props.mode === "static") return;
        const value = parseFloat(event.target.value);
        if (!isNaN(value)) {
            switch (key) {
                case "incomingDamage":
                    incomingDamageRef.value = value;
                    break;
                case "armorToughness":
                    armorToughnessRef.value = value;
                    break;
                case "minDamage":
                    minDamageRef.value = value;
                    break;
                case "maxDamage":
                    maxDamageRef.value = value;
                    break;
                case "maxArmorPoints":
                    maxArmorPointsRef.value = value;
                    break;
            }
            debugInfo.value = `${key} changed to ${value}`;
            updateChart();
        }
    };

    const toggleEdition = () => {
        if (props.mode === "static") return;
        isJavaEditionRef.value = !isJavaEditionRef.value;
        updateChart();
    };
</script>

<template>
    <div class="minecraft-damage-chart" :class="{ 'dark-mode': isDark }">
        <div v-if="mode === 'interactive'" class="input-container">
            <div
                class="input-group"
                v-for="key in [
                    'incomingDamage',
                    'armorToughness',
                    'minDamage',
                    'maxDamage',
                    'maxArmorPoints',
                ]"
                :key="key"
            >
                <label :for="key">{{ localText[key] }}</label>
                <input
                    :id="key"
                    type="number"
                    :value="
                        key === 'incomingDamage'
                            ? incomingDamageRef
                            : key === 'armorToughness'
                            ? armorToughnessRef
                            : key === 'minDamage'
                            ? minDamageRef
                            : key === 'maxDamage'
                            ? maxDamageRef
                            : maxArmorPointsRef
                    "
                    @input="(event) => handleInput(event, key)"
                />
            </div>
            <div class="input-group">
                <label>&nbsp;</label>
                <button
                    @click="toggleEdition"
                    :class="{
                        java: isJavaEditionRef,
                        bedrock: !isJavaEditionRef,
                    }"
                >
                    {{
                        isJavaEditionRef
                            ? localText.javaEdition
                            : localText.bedrockEdition
                    }}
                </button>
            </div>
        </div>
        <div class="chart-container">
            <canvas ref="chartRef"></canvas>
        </div>
        <div v-if="mode === 'interactive'" class="debug-info">
            {{ debugInfo }}
        </div>
    </div>
</template>

<style scoped>
    :root {
        --button-bg: #f6f6f7;
        --button-color: #333;
        --button-hover-bg: #e0e0e0;
    }

    .dark-mode {
        --button-bg: #333333;
        --button-color: #fff;
        --button-hover-bg: #5a5a5a;
    }

    .minecraft-damage-chart {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
            Helvetica, Arial, sans-serif;
        background-color: var(--vp-c-bg);
        color: var(--vp-c-text-1);
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .input-container {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
        margin-bottom: 20px;
    }

    .input-group {
        flex: 1 1 calc(33.333% - 15px);
        display: flex;
        flex-direction: column;
    }

    label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: var(--vp-c-text-1);
    }

    input,
    button {
        width: 100%;
        padding: 8px;
        border: 1px solid var(--vp-c-divider);
        border-radius: 4px;
        background-color: var(--vp-c-bg-alt);
        color: var(--vp-c-text-1);
        font-size: 14px;
        transition: all 0.3s ease;
    }

    input:focus,
    button:focus {
        outline: none;
        border-color: var(--vp-c-brand);
        box-shadow: 0 0 0 2px var(--vp-c-brand-light);
    }

    button {
        cursor: pointer;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        background-color: var(--button-bg);
        color: var(--button-color);
    }

    button:hover {
        background-color: var(--button-hover-bg);
    }

    .chart-container {
        height: 50vh;
        min-height: 300px;
        width: 100%;
        background-color: var(--vp-c-bg-alt);
        border-radius: 4px;
        padding: 10px;
    }

    .debug-info {
        margin-top: 10px;
        font-size: 12px;
        color: var(--vp-c-text-2);
    }

    @media (max-width: 768px) {
        .input-group {
            flex: 1 1 100%;
        }

        .chart-container {
            height: 40vh;
            min-height: 250px;
        }

        .minecraft-damage-chart {
            padding: 10px;
        }

        .input-container {
            gap: 10px;
        }
    }
</style>
