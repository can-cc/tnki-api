import Vue from 'vue'
import Router from 'vue-router'
import SignIn from '@/components/SignIn'
import SignUp from '@/components/SignUp'
import Home from '@/components/Home'
import Post from '@/components/Post'
import Dash from '@/components/Dash'
import Learn from '@/components/Learn'
import LearnComplete from '@/components/LearnComplete'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
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
      path: '/home',
      name: 'Dash',
      component: Dash
    },
    {
      path: '/learn',
      name: 'Learn',
      component: Learn
    },
    {
      path: '/learn-complete',
      name: 'LearnComplete',
      component: LearnComplete
    }
  ]
})
