<template>
  <div class="learn-complete-page">

    <div class="main-container">
      <h1>Tnki</h1>
      <img alt="" src="../assets/logo.png"/>
    </div>

    <canvas id="home-canvas"></canvas>

  </div>
</template>

<script>
  import homeEffect from '@/util/home-effect'

  export default {
    name: 'HomePage',
    mounted: function () {
      const canvasDom = this.$el.querySelector('#home-canvas')
      homeEffect(canvasDom)
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1 {
    color: #409EFF;
    font-family: "Verdana";
    margin-bottom: -5px;
  }

  img {
    width: 200px;
  }

  .main-container {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -70%);
  }

  #home-canvas {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
  }
</style>
