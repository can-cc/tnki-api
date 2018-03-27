<template>
  <div id="app">
    <Nav/>
    <router-view/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Nav from '@/components/Nav.vue';

@Component({
  components: {
    Nav
  }
})
export default class App extends Vue {}
</script>

<style lang="stylus">
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
  color: #2c3e50;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity .3s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active in below version 2.1.8 */ {
  opacity: 0;
}
</style>
