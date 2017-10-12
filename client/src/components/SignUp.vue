<template>
  <div class="signup-page">
    <div>Sign Up</div>
    <form v-on:submit="signUp($event)">
      <div class="form-item">
        <label>
          <span>Email</span>
          <el-input v-model="email" name="tnki-email" type="email" placeholder="Email"></el-input>
        </label>
      </div>

      <div class="form-item">
        <label>
          <span>Password</span>
          <el-input v-model="password" name="tnki-password" type="password" placeholder="Password" minlength="6"></el-input>
        </label>
      </div>


      <div class="form-item">
        <label>
          <span>Repeat Password</span>
          <el-input v-model="repeatPassword" name="tnki-repeat-password" type="password" placeholder="Repeat Password" minlenght="6"></el-input>
        </label>
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

  form {
    padding: 0 10px;
  }

  .form-item {
    margin-bottom: 10px;
  }

  .form-item label {
    display: flex;
    align-items: center;
  }

  .form-item span {
    display: inline-block;
    width: 110px;
    text-align: left;
  }

</style>
