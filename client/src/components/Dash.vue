<template>
  <div class="dash-page">
    <div class="statistics">
      <div v-for="item in [{name: 'Learned', key: 'learn_time'}, {name: 'Need learn', key: 'need_learn_count'}, {name: 'All Finish', key: 'all_finish'}]" class="statistics-grid">
        <div class="name">
          {{item.name}}
        </div>
        <div class="value">
          {{statisticsData ? statisticsData[item.key] : '-'}}
        </div>
      </div>
    </div>

    <div>
      <router-link class="go-learn-button" to="learn">
        <div>Learn</div>
      </router-link>
    </div>

  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'DashPage',
    data () {
      return {
        statisticsData: null
      }
    },
    async created () {
      const statisticsData = await axios.get('/api/daily/statistics')
      this.statisticsData = statisticsData.data
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
  .go-learn-button {
    display: block;
    height: 80px;
    width: 80px;
    line-height: 80px;
    background-color: #409EFF;
    color: white;
    border-radius: 50%;
    font-size: 1.7rem;
    font-weight: 700;
  }

  .go-learn-button div {
    display: inline-block;
    vertical-align: middle;
  }

  .statistics {
    display: flex;
  }
</style>
