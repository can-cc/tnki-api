// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Button from './components/widget/Button'
import axios from 'axios'

axios.defaults.headers.common['jwt'] = window.localStorage.getItem('jwt')

Vue.config.productionTip = false
Vue.component('Button', Button)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
