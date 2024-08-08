<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useData } from 'vitepress'

const { isDark } = useData()

const chartRef = ref(null)
let chartInstance = null

const incomingDamage = ref(20)
const armorToughness = ref(0)
const minDamage = ref(0)
const maxDamage = ref(25)

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
  armorPoints: isChinesePath.value ? '护甲值' : 'Armor Points'
}))

const calculateDamageReduction = (armor, toughness, damage) => {
  const defensePoints = Math.min(20, Math.max(armor / 5, armor - damage / (2 + toughness / 4)))
  return defensePoints / 25
}

const chartData = computed(() => {
  const labels = []
  const actualDamage = []
  const damageReduction = []

  for (let armor = 0; armor <= 20; armor += 1) {
    labels.push(armor)
    const reduction = calculateDamageReduction(armor, armorToughness.value, incomingDamage.value)
    actualDamage.push(incomingDamage.value * (1 - reduction))
    damageReduction.push(reduction * 100)
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
      }
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

watch([incomingDamage, armorToughness, minDamage, maxDamage, isDark], () => {
  updateChart()
})

const handleInput = (event, key) => {
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
    }
    debugInfo.value = `${key} changed to ${value}`
    updateChart()
  }
}
</script>

<template>
  <div class="minecraft-damage-chart" :class="{ 'dark-mode': isDark }">
    <div class="input-container">
      <div class="input-group" v-for="key in ['incomingDamage', 'armorToughness', 'minDamage', 'maxDamage']" :key="key">
        <label :for="key">{{ localText[key] }}</label>
        <input
          :id="key"
          type="number"
          :value="key === 'incomingDamage' ? incomingDamage : key === 'armorToughness' ? armorToughness : key === 'minDamage' ? minDamage : maxDamage"
          @input="(event) => handleInput(event, key)"
        />
      </div>
    </div>
    <div class="chart-container">
      <canvas ref="chartRef"></canvas>
    </div>
    <div class="debug-info">{{ debugInfo }}</div>
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
  flex: 1 1 200px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 4px;
  background-color: var(--vp-c-bg-alt);
  color: var(--vp-c-text-1);
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
  color: #888;
}

.dark-mode {
  --vp-c-bg: #1a1a1a;
  --vp-c-bg-alt: #2a2a2a;
  --vp-c-text-1: #ffffff;
  --vp-c-divider: #4a4a4a;
}
</style>