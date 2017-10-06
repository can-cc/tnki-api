<template>
  <div class="dash-page">


  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'DashPage',
    data () {
      return {
        inputFront: '# input card frontend',
        inputBackend: '## input card backend'
      }
    },
    methods: {
      async post (event) {
        event.preventDefault()
        await axios.post('/api/cards', {
          front: this.inputFront,
          back: this.inputBackend
        })
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
