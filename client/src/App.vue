<template>
  <div id="app">
    <TopBar />
    <router-view></router-view>
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
    text-align: center;
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
</style>
