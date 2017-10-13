<template>
	<div class="container">
    <div>
		  <router-link class="top-bar-link" to="/home">LEARN</router-link>
    </div>
		<img class="logo" alt="logo" src="../assets/logo.png"/>
    <div>
		  <router-link class="top-bar-link" to="/home">PROFILE</router-link>
      <router-link class="post-link" to="/post">
        <i class="el-icon-edit"></i>
      </router-link>
    </div>
	</div>
</template>

<script>
  export default {
    name: 'TopBar'
  }
</script>

<style scoped>
  .container {
	  display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    z-index: 99;
    padding: 5px 0px 5px;
    box-shadow: 0 0 3px #e8e8e8;
  }

  .container > div {
    width: 170px;
    height: 28px;
    line-height: 28px;
    position: relative;
    text-align: center;
    overflow: hidden;
  }

  .logo {
    width: 30px;
	  height: 30px;
    border-radius: 50%;
    box-shadow: 0 0 5px #f8f8f8;
    opacity: 0.95;
    margin: 0 20px;
  }

  .top-bar-link {
    font-weight: 900;
    font-family: "HelveticaNeue-Light","Helvetica Neue Light","Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;
  }

  .post-link {
    display: block;
    height: 16px;
    width: 16px;
    float: right;
    position: relative;
    top: 3.5px;
    left: -8px;
    color: #409EFF;
    border: 2px solid #409EFF;
    border-radius: 50%;
    padding: 1px;
  }
  .post-link i {
    position: absolute;
    top: 1px;
    left: 1px;
  }
</style>
