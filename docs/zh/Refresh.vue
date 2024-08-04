<template>
    <v-btn
      color="primary"
      @click="refreshPage"
      :loading="isLoading"
    >
      <v-icon left>mdi-refresh</v-icon>
      如果此处未渲染表格则点击此按钮刷新界面
    </v-btn>
  </template>
  
  <script>
  export default {
    data() {
      return {
        isLoading: false
      }
    },
    methods: {
      refreshPage() {
        this.isLoading = true;
        setTimeout(() => {
          window.location.reload();
        }, 500);
      }
    }
  }
  </script>