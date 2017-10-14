import Vue from 'vue'
import Router from 'vue-router'

const Post = () => import(/* webpackChunkName: "post" */'@/components/Post')
const Home = () => import(/* webpackChunkName: "home" */'@/components/Home')
const Dash = () => import(/* webpackChunkName: "dash" */'@/components/Dash')
const Learn = () => import(/* webpackChunkName: "dash" */'@/components/Learn')
const LearnComplete = () => import(/* webpackChunkName: "learn-complete" */'@/components/LearnComplete')
const SignIn = () => import(/* webpackChunkName: "sign-in" */'@/components/SignIn')
const SignUp = () => import(/* webpackChunkName: "sign-up" */'@/components/SignUp')

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
