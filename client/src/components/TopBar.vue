<template>
	<div class="container">
		<div>Learn</div>
		<img class="logo" alt="" src="../assets/logo.png"/>
		<div>Profile</div>
	</div>
</template>

<script>
  export default {
    name: 'TopBar'
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
 .container {
	 display: flex;
	 border-bottom: 1px solid #999;
 }

 .logo {
   width: 30px;
	 height: 30px;
 }
</style>
