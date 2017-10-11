<template>
  <div class="learn-page">
    <h1>Learn</h1>

    <div class="card-container" v-if="cards[0]">
      <div v-html="mark(cards[0].front_text)"></div>

      <div v-if="learningOpen">
        <hr/>
        <div v-html="mark(cards[0].back_text)"></div>
      </div>

      <div class="operation" v-if="!learningOpen">
        <div v-on:click="show()">Show Back</div>
      </div>

      <div class="operation__open" v-if="learningOpen">
        <div v-on:click="open(0)">Too Easy</div>
        <div v-on:click="open(1)">Remeber</div>
        <div v-on:click="open(2)">Forgot</div>
      </div>

    </div>

  </div>
</template>

<script>
  import axios from 'axios'
  import router from '@/router/index'
  import marked from 'marked'
  import fp from 'lodash/fp'

  export default {
    name: 'LearnPage',
    data () {
      return {
        inputFront: '# input card frontend',
        inputBackend: '## input card backend',
        cards: [],
        learningOpen: false
      }
    },
    created () {
      axios.get('/api/cards').then(response => {
        this.cards = response.data
        if (!this.cards.length) {
          return router.push('/learn-complete')
        }
      })
    },
    methods: {
      async open (memoryLevel) {
        await axios.post(`/api/cards/${this.cards[0].id}/memory`, {
          memoryLevel: memoryLevel
        })
        if (!this.cards.length) {
          return router.push('/learn-complete')
        }
        this.cards = fp.drop(1, this.cards)
        this.learningOpen = false
      },
      show () {
        this.learningOpen = true
      },
      mark (rawMd) {
        return marked(rawMd, { sanitize: true })
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .card-container {
    padding: 20px;
    box-shadow: 0 0 10px #999;
    border-radius: 4px;
    transition: all 300ms ease-in-out;
  }

  .operation__open, .operation {
    display: flex;
    justify-content: space-around;
  }

  .operation__open > div, .operation > div {
    cursor: pointer;
    padding: 3px 10px;
    border-radius: 3px;
  }

  .operation__open  > div:hover, .operation > div:hover {
    background-color: #f8f8f8;
  }
</style>
