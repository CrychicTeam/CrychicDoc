<template>
    <div class="video-container">
      <iframe
        :src="'https://www.youtube.com/embed/' + videoId"
        frameborder="0"
        allow="autoplay; encrypted-media"
        allowfullscreen
      ></iframe>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      videoId: {
        type: String,
        required: true
      }
    }
  }
  </script>
  
  <style scoped>
  .video-container {
    position: relative;
    padding-bottom: 56.25%; /* 16:9 */
    height: 0;
    overflow: hidden;
  }
  .video-container iframe {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }
  </style>