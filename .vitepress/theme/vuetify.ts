import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

export default createVuetify({
  ssr: false,
  components,
  theme: {
    themes: {
      light: {
        colors: {
          primary: '#1867C0',
          secondary: '#5CBBF6',
        },
      },
    },
  },
})