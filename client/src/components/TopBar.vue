<template>
	<div class="container">
		<router-link class="top-bar-link" to="/home">LEARN</router-link>
		<img class="logo" alt="logo" src="../assets/logo.png"/>
		<router-link class="top-bar-link" to="/home">PROFILE</router-link>
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
    padding-left: 10px;
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
    font-family: "SourceCodePro";
  }
</style>
