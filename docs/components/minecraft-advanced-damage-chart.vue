<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useData } from 'vitepress'

const props = defineProps({
  static: {
    type: Boolean,
    default: false
  },
  presetData: {
    type: Object,
    default: () => ({
      incomingDamage: 201,
      armorToughness: 150,
      minDamage: 0,
      maxDamage: 200,
      maxArmorPoints: 50,
      isJavaEdition: true
    })
  }
})

const { isDark } = useData()

const chartRef = ref(null)
let chartInstance = null

const incomingDamage = ref(props.presetData.incomingDamage)
const armorToughness = ref(props.presetData.armorToughness)
const minDamage = ref(props.presetData.minDamage)
const maxDamage = ref(props.presetData.maxDamage)
const maxArmorPoints = ref(props.presetData.maxArmorPoints)
const isJavaEdition = ref(props.presetData.isJavaEdition)

// Debug information
const debugInfo = ref('')

const isChinesePath = ref(false)

onMounted(() => {
  isChinesePath.value = window.location.pathname.includes('/zh/') || window.location.pathname.startsWith('/zh')
  
  if (chartRef.value) {
    import('chart.js/auto').then((ChartModule) => {
      const Chart = ChartModule.default
      const ctx = chartRef.value.getContext('2d')
      chartInstance = new Chart(ctx, {
        type: 'line',
        data: chartData.value,
        options: chartOptions.value
      })
      debugInfo.value = 'Chart mounted'
    })
  }
})

const localText = computed(() => ({
  incomingDamage: isChinesePath.value ? '初始伤害' : 'Incoming Damage',
  armorToughness: isChinesePath.value ? '护甲韧性' : 'Armor Toughness',
  minDamage: isChinesePath.value ? '最小伤害' : 'Min Damage',
  maxDamage: isChinesePath.value ? '最大伤害' : 'Max Damage',
  actualDamage: isChinesePath.value ? '实际伤害' : 'Actual Damage',
  damageReduction: isChinesePath.value ? '伤害减免 (%)' : 'Damage Reduction (%)',
  armorPoints: isChinesePath.value ? '护甲值' : 'Armor Points',
  maxArmorPoints: isChinesePath.value ? '最大护甲值' : 'Max Armor Points',
  javaEdition: isChinesePath.value ? 'Java版' : 'Java Edition',
  bedrockEdition: isChinesePath.value ? '基岩版' : 'Bedrock Edition'
}))

const calculateDamage = (armor, toughness, damage, isJava) => {
  if (isJava) {
    const defensePoints = Math.min(20, Math.max(armor / 5, armor - damage / (2 + toughness / 4)))
    return damage * (1 - defensePoints / 25)
  } else {
    return damage * (1 - Math.min(20, armor) * 0.04)
  }
}

const chartData = computed(() => {
  const labels = []
  const actualDamage = []
  const damageReduction = []

  for (let armor = 0; armor <= maxArmorPoints.value; armor += 1) {
    labels.push(armor)
    const damage = calculateDamage(armor, armorToughness.value, incomingDamage.value, isJavaEdition.value)
    const clampedDamage = Math.max(minDamage.value, Math.min(maxDamage.value, damage))
    actualDamage.push(Number(clampedDamage.toFixed(2)))
    damageReduction.push(Number(((1 - clampedDamage / incomingDamage.value) * 100).toFixed(2)))
  }

  return {
    labels,
    datasets: [
      {
        label: localText.value.actualDamage,
        borderColor: isDark.value ? '#ff6384' : '#ff3860',
        backgroundColor: isDark.value ? 'rgba(255, 99, 132, 0.2)' : 'rgba(255, 56, 96, 0.2)',
        data: actualDamage,
        yAxisID: 'y',
      },
      {
        label: localText.value.damageReduction,
        borderColor: isDark.value ? '#36a2eb' : '#3273dc',
        backgroundColor: isDark.value ? 'rgba(54, 162, 235, 0.2)' : 'rgba(50, 115, 220, 0.2)',
        data: damageReduction,
        yAxisID: 'y1',
      }
    ]
  }
})

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    x: {
      title: {
        display: true,
        text: localText.value.armorPoints,
        color: isDark.value ? '#ffffff' : '#363636'
      },
      ticks: {
        color: isDark.value ? '#ffffff' : '#363636'
      },
      grid: {
        color: isDark.value ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)'
      },
      max: maxArmorPoints.value
    },
    y: {
      type: 'linear',
      display: true,
      position: 'left',
      title: {
        display: true,
        text: localText.value.actualDamage,
        color: isDark.value ? '#ffffff' : '#363636'
      },
      min: minDamage.value,
      max: maxDamage.value,
      ticks: {
        color: isDark.value ? '#ffffff' : '#363636'
      },
      grid: {
        color: isDark.value ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)'
      }
    },
    y1: {
      type: 'linear',
      display: true,
      position: 'right',
      title: {
        display: true,
        text: localText.value.damageReduction,
        color: isDark.value ? '#ffffff' : '#363636'
      },
      min: 0,
      max: 100,
      ticks: {
        color: isDark.value ? '#ffffff' : '#363636'
      },
      grid: {
        drawOnChartArea: false,
        color: isDark.value ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)'
      }
    }
  },
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#ffffff' : '#363636'
      }
    },
    tooltip: {
      mode: 'index',
      intersect: false,
      callbacks: {
        label: function(context) {
          let label = context.dataset.label || '';
          if (label) {
            label += ': ';
          }
          if (context.parsed.y !== null) {
            label += context.parsed.y.toFixed(2);
          }
          return label;
        }
      }
    }
  }
}))

const updateChart = () => {
  if (chartInstance) {
    chartInstance.data = chartData.value
    chartInstance.options = chartOptions.value
    chartInstance.update()
    debugInfo.value = `Chart updated at ${new Date().toLocaleTimeString()}`
  }
}

watch([incomingDamage, armorToughness, minDamage, maxDamage, maxArmorPoints, isJavaEdition, isDark], () => {
  updateChart()
})

const handleInput = (event, key) => {
  if (props.static) return
  const value = parseFloat(event.target.value)
  if (!isNaN(value)) {
    switch(key) {
      case 'incomingDamage':
        incomingDamage.value = value
        break
      case 'armorToughness':
        armorToughness.value = value
        break
      case 'minDamage':
        minDamage.value = value
        break
      case 'maxDamage':
        maxDamage.value = value
        break
      case 'maxArmorPoints':
        maxArmorPoints.value = value
        break
    }
    debugInfo.value = `${key} changed to ${value}`
    updateChart()
  }
}

const toggleEdition = () => {
  if (props.static) return
  isJavaEdition.value = !isJavaEdition.value
  updateChart()
}
</script>

<template>
  <div class="minecraft-damage-chart" :class="{ 'dark-mode': isDark, 'static-mode': static }">
    <div v-if="!static" class="input-container">
      <div class="input-group" v-for="key in ['incomingDamage', 'armorToughness', 'minDamage', 'maxDamage', 'maxArmorPoints']" :key="key">
        <label :for="key">{{ localText[key] }}</label>
        <input
          :id="key"
          type="number"
          :value="key === 'incomingDamage' ? incomingDamage : key === 'armorToughness' ? armorToughness : key === 'minDamage' ? minDamage : key === 'maxDamage' ? maxDamage : maxArmorPoints"
          @input="(event) => handleInput(event, key)"
        />
      </div>
      <div class="input-group">
        <label>&nbsp;</label>
        <button @click="toggleEdition" :class="{ 'java': isJavaEdition, 'bedrock': !isJavaEdition }">
          {{ isJavaEdition ? localText.javaEdition : localText.bedrockEdition }}
        </button>
      </div>
    </div>
    <div class="chart-container">
      <canvas ref="chartRef"></canvas>
    </div>
    <div v-if="!static" class="debug-info">{{ debugInfo }}</div>
  </div>
</template>

<style scoped>
.minecraft-damage-chart {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
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

input, button {
  width: 100%;
  padding: 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 4px;
  background-color: var(--vp-c-bg-alt);
  color: var(--vp-c-text-1);
  font-size: 14px;
  transition: all 0.3s ease;
}

input:focus, button:focus {
  outline: none;
  border-color: var(--vp-c-brand);
  box-shadow: 0 0 0 2px var(--vp-c-brand-light);
}

button {
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  background-color: var(--vp-c-bg-alt);
  color: var(--vp-c-text-1);
}

button:hover {
  background-color: var(--vp-c-bg-mute);
}

.chart-container {
  height: 400px;
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

.dark-mode {
  --vp-c-bg: #1a1a1a;
  --vp-c-bg-alt: #2a2a2a;
  --vp-c-bg-mute: #3a3a3a;
  --vp-c-text-1: #ffffff;
  --vp-c-text-2: #aaaaaa;
  --vp-c-divider: #4a4a4a;
}

.static-info {
  margin-bottom: 20px;
}

.static-info p {
  margin: 5px 0;
}

@media (max-width: 768px) {
  .input-group {
    flex: 1 1 100%;
  }
}
</style>
