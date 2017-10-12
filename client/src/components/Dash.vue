<template>
  <div class="dash-page">
    <div class="total-days-container">
      <span class="day-number">{{statisticsData ? statisticsData.total_days : '-'}}</span>
      <span class="day-text">days</span>
      <i class="el-icon-date"></i>
    </div>
    <div class="statistics">
      <div v-for="item in [{name: 'Learned', key: 'learn_time'}, {name: 'Need learn', key: 'need_learn_count'}, {name: 'All Finish', key: 'all_finish'}]" class="statistics-grid">
        <div class="value">{{statisticsData ? statisticsData[item.key] : '-'}}</div>
        <div class="name">{{item.name}}</div>
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
    font-size: 1.5rem;
    font-weight: 700;
    border: 5px solid #4a9de0;
    margin: 30px auto 0;
  }

  .go-learn-button div {
    display: block;
    vertical-align: middle;
  }

  .statistics-grid {
    width: 100%;
    color: #878D99;
  }
  .statistics-grid .value {
    font-weight: 900;
    font-size: 1.5rem;
    letter-spacing: 2px;
    line-height: 1rem;
    color: #EB9E05;
  }
  .statistics-grid .name {
    font-weight: 300;
    font-size: 0.8rem;
  }

  .statistics {
    display: flex;
    padding: 0 0 10px;
  }

  .total-days-container {
    margin: 30px;
  }

  .total-days-container .day-number {
    font-weight: 900;
    font-size: 4rem;
    color: #67C23A;
    margin-left: 58px;
  }

  .total-days-container .day-text {
    color: #409EFF;
    font-weight: 900;
  }

  .total-days-container .el-icon-date {
    color: #409EFF;
  }
</style>
