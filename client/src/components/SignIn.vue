<template>
  <div class="signin-page">
    <form v-on:submit="signIn($event)">
      <div>
        <label for="">Email</label>
        <input v-model="email" name="" type="email" value=""/>
      </div>

      <div>
        <label for="">Password</label>
        <input v-model="password" name="" type="password" value=""/>
      </div>

      <button type="submit">SignIn</button>

    </form>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'SignInPage',
    data () {
      return {
        password: '',
        email: ''
      }
    },
    methods: {
      async signIn (event) {
        event.preventDefault()
        await axios.post('/api/signin', {
          email: this.email,
          password: this.password
        })
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
</style>
