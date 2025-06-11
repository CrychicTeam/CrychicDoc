import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

/**
 * Creates a Vuetify instance with SSR support.
 */
export default createVuetify({
  ssr: true,
  components,
  directives
})