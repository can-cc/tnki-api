<template>
  <div class="dash-page">
    <el-card :body-style="{padding: '0'}">
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
    </el-card>

    <div>
      <router-link class="go-learn-button" to="learn">
        <div>Learn</div>
      </router-link>
    </div>

    <div class="post-link-container">
      <router-link to="/post">
        <i class="el-icon-d-arrow-right"></i>
        add new card
      </router-link>
    </div>

    <div class="ds-container" v-if="ds">
      <div class="content">{{ds.content}}</div>
      <div class="note">{{ds.note}}</div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import jsonp from 'jsonp'

  export default {
    name: 'DashPage',
    data () {
      return {
        statisticsData: null,
        ds: null
      }
    },
    created () {
      console.log(process)
      axios.get(`/api/daily/statistics?timestamp=${new Date().getTime()}`).then(response => {
        this.statisticsData = response.data
      })
      jsonp(`${process.env.production ? 'https' : 'http'}://open.iciba.com/dsapi/`, null, (err, data) => {
        if (data) {
          this.ds = data
        }
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
  .el-card {
    margin: 10px;
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
    padding: 10px 0 10px;
  }

  .total-days-container {
    margin: 30px auto 10px;
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

  .post-link-container {
    margin-top: 15px;
  }
  .post-link-container a {
    font-weight: 700;
    letter-spacing: -0.5px;
  }
  .post-link-container .el-icon-d-arrow-right {
    font-size: 10px;
    margin-left: -4px;
  }
  .ds-container {
    font-size: 12px;
    font-weight: 500;
    color: #67C23A;
    position: absolute;
    bottom: 10px;
    width: 100%;
  }
</style>
