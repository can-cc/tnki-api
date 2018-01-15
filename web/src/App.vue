<template>
  <div id="app">
    <TopBar />
    <transition name="fade">
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
  import TopBar from './components/TopBar'

  export default {
    name: 'app',
    components: {
      TopBar: TopBar
    }
  }
</script>

<style>
  html, body {
    margin: 0;
  }

  hr {
    background-color: #444;
    border-width: 0;
    color: #444;
    height: 2px;
    line-height: 0;
    page-break-after: always;
    width: 80%;
  }

  a {
    text-decoration: none;
    color: #4688f1;
  }

  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .3s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active in below version 2.1.8 */ {
    opacity: 0;
  }
</style>
