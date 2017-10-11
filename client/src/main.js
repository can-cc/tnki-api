// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'

import 'element-ui/lib/theme-chalk/index.css'
import { Button, Select } from 'element-ui'

axios.defaults.headers.common['jwt'] = window.localStorage.getItem('jwt')

Vue.config.productionTip = false
Vue.component('Button', Button)
Vue.component(Button.name, Button)
Vue.component(Select.name, Select)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
