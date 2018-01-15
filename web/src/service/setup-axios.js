import axios from 'axios'
import router from '@/router'

axios.defaults.headers.common['jwt'] = window.localStorage.getItem('jwt')
axios.interceptors.response.use(function (response) {
  return response
}, function (error) {
  if (error.response.status === 401) {
    router.push('/signin')
  }
  return Promise.reject(error)
})
