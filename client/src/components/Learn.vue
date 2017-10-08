<template>
  <div class="learn-page">
    <h1>Learn</h1>

    <div v-if="learningCard">

    </div>

  </div>
</template>

<script>
  import axios from 'axios'
  import router from '@/router/index'

  export default {
    name: 'LearnPage',
    data () {
      return {
        inputFront: '# input card frontend',
        inputBackend: '## input card backend',
        cards: [],
        learningCard: null
      }
    },
    created () {
      axios.get('/api/cards').then(response => {
        this.cards = response.data
        if (!this.cards.length) {
          router.push('/dash')
        }
        this.learningCard = this.cards[0]
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
