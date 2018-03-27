<template>
  <nav>
	  <div class="container">
		  <img class="logo" alt="logo" src="../assets/logo.png"/>
      <div>
		    <router-link class="top-bar-link" to="/home">
          <i class="el-icon-search"/>
          learn
        </router-link>
        <router-link class="post-link" to="/post">
          <i class="el-icon-edit-outline"/>
          create
        </router-link>
      </div>
	  </div>
  </nav>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class Nav extends Vue {
  @Prop() private msg!: string;
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="stylus">
nav
  height: 40px
  width:  100%
  background: #333

.container
  z-index: 99
  width: 700px
  margin: 0 auto
  display: flex
  justify-content: flex-start
  align-items: center
  position: relative
  padding: 5px 0px 5px

.container > div
  line-height: 30px
  position: relative
  text-align: center
  overflow: hidden

.logo
  width: 22px
  height: 22px
  border-radius: 50%
  opacity: 0.95
  margin: 0 20px
  border-radius: 50%

a
  color: white
  width: 80px
  display: inline-block
  font-size: 18px
  vertical-align: bottom

.top-bar-link
  font-family: "HelveticaNeue-Light","Helvetica Neue Light","Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif

</style>
