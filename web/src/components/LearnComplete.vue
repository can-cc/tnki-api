<template>
  <div class="learn-complete-page">

    <el-card>
      <h2>Congratulations, learn complete!</h2>
    </el-card>

    <el-button>
      <router-link to="home">Go Home</router-link>
    </el-button>

  </div>
</template>

<script>
  export default {
    name: 'LearnCompletePage'
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h2 {
    color: #67C23A;
  }

  .el-card {
    margin: 50px 20px;
  }
</style>
