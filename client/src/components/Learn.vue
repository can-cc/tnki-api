<template>
  <div class="learn-page">
    <h1>Learn</h1>

    <div class="card-container" v-if="learningCard">
      <div v-html="mark(learningCard.front_text)"></div>

      <div class="operation" v-if="!learningOpen">
        <div v-on:click="open(0)">Too Easy</div>
        <div v-on:click="open(1)">Remeber</div>
        <div v-on:click="open(2)">Forgot</div>
      </div>

      <div class="operation__open" v-if="learningOpen">
        <div v-on:click="open(0)">Next</div>
      </div>

    </div>

  </div>
</template>

<script>
  import axios from 'axios'
  import router from '@/router/index'
  import marked from 'marked'

  export default {
    name: 'LearnPage',
    data () {
      return {
        inputFront: '# input card frontend',
        inputBackend: '## input card backend',
        cards: [],
        learningCard: null,
        learningOpen: false
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
      async open (memoryLevel) {
        await axios.post(`/api/cards/${this.learningCard.card_id}/memory`, {
          memory_level: memoryLevel
        })
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
    display: inline-block;
    padding: 20px;
    box-shadow: 0 0 10px #999;
    border-radius: 4px;
  }

  .operation__open, .operation {
    display: flex;
    justify-content: space-around;
  }

  .operation__open > div, .operation > div {
    cursor: pointer;
    padding: 3px 10px;
  }

  .operation__open  > div:hover, .operation > div:hover {
    background-color: #f8f8f8;
  }
</style>
