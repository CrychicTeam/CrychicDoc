<template>
    <div class="carousel carousel-margin">
      <div class="carousel-inner" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div class="carousel-item" v-for="(image, index) in images" :key="index">
          <img :src="image.src" :alt="image.alt">
        </div>
      </div>
      <button class="carousel-control prev" @click="prev" aria-label="Previous slide">&lt;</button>
      <button class="carousel-control next" @click="next" aria-label="Next slide">&gt;</button>
      <div class="carousel-indicators">
        <button 
          v-for="(_, index) in images" 
          :key="index" 
          :class="{ active: index === currentIndex }"
          @click="goToSlide(index)"
          :aria-label="`Go to slide ${index + 1}`"
        ></button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted } from 'vue'
  
  const props = defineProps({
    images: {
      type: Array,
      required: true,
      default: () => []
    }
  })
  
  const currentIndex = ref(0)
  const timer = ref(null)
  
  const next = () => {
    currentIndex.value = (currentIndex.value + 1) % props.images.length
  }
  
  const prev = () => {
    currentIndex.value = (currentIndex.value - 1 + props.images.length) % props.images.length
  }
  
  const goToSlide = (index) => {
    currentIndex.value = index
  }
  
  const startTimer = () => {
    timer.value = setInterval(next, 5000) // 每5秒自动切换一次
  }
  
  const stopTimer = () => {
    clearInterval(timer.value)
  }
  
  onMounted(() => {
    startTimer()
    console.log('Carousel mounted, images:', props.images)
  })
  
  onUnmounted(() => {
    stopTimer()
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
  
  .carousel-item {
    flex: 0 0 100%;
    height: 100%;
  }
  
  .carousel-item img {
    width: 100%;
    height: 100%;
    object-fit: cover;
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