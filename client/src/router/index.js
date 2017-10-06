import Vue from 'vue'
import Router from 'vue-router'
import SignIn from '@/components/SignIn'
import SignUp from '@/components/SignUp'
import Hello from '@/components/Hello'
import Post from '@/components/Post'
import Dash from '@/components/Dash'
import Learn from '@/components/Learn'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/signin',
      name: 'SignIn',
      component: SignIn
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/post',
      name: 'Post',
      component: Post
    },
    {
      path: '/dash',
      name: 'Dash',
      component: Dash
    },
    {
      path: '/learn',
      name: 'Learn',
      component: Learn
    }
  ]
})
