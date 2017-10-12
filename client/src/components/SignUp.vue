<template>
  <div class="signup-page">
    <h1>Tnki</h1>
    <h2>Sign Up</h2>
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
          <el-input v-model="repeatPassword" name="tnki-repeat-password" type="password" placeholder="Repeat Password" minlength="6"></el-input>
        </label>
      </div>

      <div class="button-container">
        <el-button native-type="submit" type="primary" plain>Sign Up</el-button>
      </div>

      <div class="tip">
        Already have an account? <router-link to="/signin">Sign In</router-link>
      </div>

    </form>
  </div>
</template>

<script>
  import axios from 'axios'
  import router from '@/router'

  export default {
    name: 'SignUpPage',
    data () {
      return {
        password: '',
        email: '',
        repeatPassword: ''
      }
    },
    methods: {
      async signUp (event) {
        try {
          event.preventDefault()
          if (this.password !== this.repeatPassword) {
            return this.$message.error('Password not match!')
          }
          await axios.post('/api/signup', {
            email: this.email,
            password: this.password
          })
          this.$message({
            message: 'Sign up sccuess! Please sign in',
            type: 'success'
          })
          router.push('/signin')
        } catch (error) {
          this.$message.error('Sign up unknown error.')
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1, h2 {
    color: #409EFF;
    font-family: "Verdana";
  }

  h2 {
    margin-top: -1.5rem;
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
    color: #409EFF;
    font-weight: bolder;
  }

  .button-container {
    margin-top: 10px;
  }

  .tip {
    margin-top: 10px;
  }

  .tip a {
    color: #409EFE
  }
</style>
