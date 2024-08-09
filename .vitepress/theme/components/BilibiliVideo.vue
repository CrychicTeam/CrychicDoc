<template>
    <div class="video-container">
      <iframe
        :src="'//player.bilibili.com/player.html?bvid=' + bvid"
        scrolling="no"
        border="0"
        frameborder="no"
        framespacing="0"
        allowfullscreen="true"
      ></iframe>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      bvid: {
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