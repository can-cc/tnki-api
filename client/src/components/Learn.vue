<template>
  <div class="learn-page">
    <h1>Learn</h1>

  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'LearnPage',
    data () {
      return {
        inputFront: '# input card frontend',
        inputBackend: '## input card backend'
      }
    },
    created () {
      axios.get('/api/cards').then(response => {
        console.log(response.data)
      })
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
