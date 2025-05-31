import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

/**
 * Creates a Vuetify instance with SSR support and custom theme configuration
 */
export default createVuetify({
  ssr: true,
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#1867C0',
          secondary: '#5CBBF6',
          accent: '#82B1FF',
          error: '#FF5252',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FFC107',
        },
      },
      dark: {
        colors: {
          primary: '#2196F3',
          secondary: '#424242',
          accent: '#FF4081',
          error: '#FF5252',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FB8C00',
        },
      },
    },
  },
  defaults: {
    VCard: {
      flat: true,
    },
    VBtn: {
      color: 'primary',
      variant: 'elevated',
    },
  },
})