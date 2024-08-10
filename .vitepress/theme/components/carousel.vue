<template>
  <div class="carousel carousel-margin">
    <div class="carousel-inner" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
      <slot></slot>
    </div>
    <button class="carousel-control prev" @click="prev" aria-label="Previous slide">&lt;</button>
    <button class="carousel-control next" @click="next" aria-label="Next slide">&gt;</button>
    <div class="carousel-indicators">
      <button 
        v-for="(_, index) in slides" 
        :key="index" 
        :class="{ active: index === currentIndex }"
        @click="goToSlide(index)"
        :aria-label="`Go to slide ${index + 1}`"
      ></button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, useSlots, watch } from 'vue'

const props = defineProps({
  cycle: {
    type: Boolean,
    default: false
  },
  interval: {
    type: Number,
    default: 5000
  }
})

const slots = useSlots()
const slides = slots.default ? slots.default() : []

const currentIndex = ref(0)
const timer = ref(null)
const lastIntervalChange = ref(Date.now())

const next = () => {
  currentIndex.value = (currentIndex.value + 1) % slides.length
}

const prev = () => {
  currentIndex.value = (currentIndex.value - 1 + slides.length) % slides.length
}

const goToSlide = (index) => {
  currentIndex.value = index
}

const startTimer = () => {
  if (props.cycle && slides.length > 1) {
    timer.value = setInterval(next, props.interval)
  }
}

const stopTimer = () => {
  if (timer.value) {
    clearInterval(timer.value)
    timer.value = null
  }
}

const restartTimer = () => {
  const now = Date.now()
  if (now - lastIntervalChange.value > 1000) { // 防抖，1秒内不重复重启
    stopTimer()
    startTimer()
    lastIntervalChange.value = now
  }
}

onMounted(() => {
  if (props.cycle) {
    startTimer()
  }
})

onUnmounted(() => {
  stopTimer()
})

watch(() => props.interval, (newInterval, oldInterval) => {
  if (props.cycle && Math.abs(newInterval - oldInterval) > 100) { // 只有当间隔变化超过100ms时才重启
    restartTimer()
  }
})

watch(() => props.cycle, (newValue) => {
  if (newValue) {
    startTimer()
  } else {
    stopTimer()
  }
})
</script>

<style scoped>
.carousel {
  position: relative;
  width: 100%;
  max-width: 700px;
  height: 350px;
  margin: 2rem auto;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.carousel-inner {
  display: flex;
  height: 100%;
  transition: transform 0.5s ease;
}

.carousel-inner > * {
  flex: 0 0 100%;
  height: 100%;
}

.carousel-control {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 18px;
  cursor: pointer;
  transition: background 0.3s;
}

.carousel-control:hover {
  background: rgba(0, 0, 0, 0.7);
}

.prev {
  left: 10px;
}

.next {
  right: 10px;
}

.carousel-indicators {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
}

.carousel-indicators button {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: none;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

.carousel-indicators button.active {
  background: white;
}
</style>