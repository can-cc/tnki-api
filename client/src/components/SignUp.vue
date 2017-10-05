<template>
  <div class="signup-page">
    <div>Sign Up</div>
    <form v-on:submit="signUp($event)">
      <div>
        <label for="">Email</label>
        <input v-model="email" name="email" type="email" value=""/>
      </div>

      <div>
        <label for="">Password</label>
        <input v-model="password" minlength="6" name="password" type="password" value=""/>
      </div>


      <div>
        <label for="">Repeat Password</label>
        <input v-model="repeatPassword" minlenght="6" name="repeat-password" type="password" value=""/>
      </div>

      <div class="error-message">
        {{errorMessage}}
      </div>


      <button type="submit">SignUp</button>

    </form>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'SignUpPage',
    data () {
      return {
        password: '',
        email: '',
        repeatPassword: '',
        errorMessage: null
      }
    },
    methods: {
      async signUp (event) {
        event.preventDefault()
        this.errorMessage = null
        if (this.password !== this.repeatPassword) {
          this.errorMessage = 'Password not match!'
          return
        }

        await axios.post('/api/signup', {
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
