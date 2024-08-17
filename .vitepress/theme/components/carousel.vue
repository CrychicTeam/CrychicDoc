<template>
  <div class="carousel" :class="{ 'carousel-margin': margin }">
    <div class="carousel-inner" ref="carouselInner" :style="carouselStyle">
      <div v-for="(slide, index) in slides" :key="index" class="carousel-item">
        <component :is="slide" />
      </div>
    </div>
    <button class="carousel-control prev" @click="prev" aria-label="Previous slide">
      <span aria-hidden="true">&lt;</span>
    </button>
    <button class="carousel-control next" @click="next" aria-label="Next slide">
      <span aria-hidden="true">&gt;</span>
    </button>
    <div class="carousel-indicators">
      <button 
        v-for="(_, index) in slides" 
        :key="index" 
        :class="{ active: index === currentIndex }"
        @click="goToSlide(index)"
        :aria-label="`Go to slide ${index + 1}`"
        :aria-current="index === currentIndex ? 'true' : 'false'"
      ></button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, useSlots, watch } from 'vue'

const props = defineProps({
  cycle: {
    type: Boolean,
    default: false
  },
  interval: {
    type: Number,
    default: 5000
  },
  margin: {
    type: Boolean,
    default: true
  }
})

const slots = useSlots()
const slides = computed(() => slots.default ? slots.default() : [])

const currentIndex = ref(0)
const timer = ref(null)
const lastInteractionTime = ref(Date.now())
const carouselInner = ref(null)

const carouselStyle = computed(() => ({
  transform: `translateX(-${currentIndex.value * 100}%)`,
  transition: 'transform 0.5s ease'
}))

const next = () => {
  currentIndex.value = (currentIndex.value + 1) % slides.value.length
  updateLastInteractionTime()
}

const prev = () => {
  currentIndex.value = (currentIndex.value - 1 + slides.value.length) % slides.value.length
  updateLastInteractionTime()
}

const goToSlide = (index) => {
  currentIndex.value = index
  updateLastInteractionTime()
}

const updateLastInteractionTime = () => {
  lastInteractionTime.value = Date.now()
}

const startTimer = () => {
  if (props.cycle && slides.value.length > 1) {
    timer.value = setInterval(() => {
      if (Date.now() - lastInteractionTime.value > props.interval) {
        next()
      }
    }, props.interval)
  }
}

const stopTimer = () => {
  if (timer.value) {
    clearInterval(timer.value)
    timer.value = null
  }
}

const handleKeydown = (event) => {
  if (event.key === 'ArrowLeft') {
    prev()
  } else if (event.key === 'ArrowRight') {
    next()
  }
}

onMounted(() => {
  if (props.cycle) {
    startTimer()
  }
  window.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  stopTimer()
  window.removeEventListener('keydown', handleKeydown)
})

watch(() => props.interval, () => {
  if (props.cycle) {
    stopTimer()
    startTimer()
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
  height: 0;
  padding-bottom: 56.25%; /* 16:9 aspect ratio */
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.carousel-margin {
  margin: 2rem auto;
}

.carousel-inner {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
}

.carousel-item {
  flex: 0 0 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.carousel-item :deep(img) {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
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
  transition: background 0.3s, opacity 0.3s;
  opacity: 0.7;
}

.carousel-control:hover, .carousel-control:focus {
  background: rgba(0, 0, 0, 0.7);
  opacity: 1;
}

.carousel-control:focus {
  outline: 2px solid white;
  outline-offset: -2px;
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
  transition: background 0.3s, transform 0.3s;
}

.carousel-indicators button.active {
  background: white;
  transform: scale(1.2);
}

.carousel-indicators button:hover, .carousel-indicators button:focus {
  background: white;
}

.carousel-indicators button:focus {
  outline: 2px solid white;
  outline-offset: 2px;
}

@media (max-width: 768px) {
  .carousel {
    padding-bottom: 75%; /* 4:3 aspect ratio for mobile */
  }

  .carousel-control {
    padding: 8px 12px;
    font-size: 16px;
  }
}
</style>