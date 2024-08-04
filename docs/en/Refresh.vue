<template>
    <v-btn
      color="primary"
      @click="refreshPage"
      :loading="isLoading"
    >
      <v-icon left>mdi-refresh</v-icon>
      If the table is not rendered here, click this button to refresh the page.
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