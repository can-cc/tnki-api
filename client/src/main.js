// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'

import 'element-ui/lib/theme-chalk/index.css'
import './style/element-reset.css'
import { Button, Input, Message } from 'element-ui'

axios.defaults.headers.common['jwt'] = window.localStorage.getItem('jwt')
axios.interceptors.response.use(function (response) {
  return response
}, function (error) {
  if (error.response.status === 401) {
    router.push('/signin')
  }
  return Promise.reject(error)
})

Vue.config.productionTip = false
Vue.component('Button', Button)
Vue.component(Button.name, Button)
Vue.component(Input.name, Input)
Vue.prototype.$message = Message

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
