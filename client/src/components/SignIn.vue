<template>
  <div class="signin-page">
    <h1>Tnki</h1>
    <h2>Sign In</h2>
    <form v-on:submit="signIn($event)">
      <div class="form-item">
        <label>
          <span>Email</span>
          <el-input v-model="email" name="tnki-email" type="email" placeholder="Email"></el-input>
        </label>
      </div>

      <div class="form-item">
        <label>
          <span>Password</span>
          <el-input v-model="password" name="tnki-password" type="password" placeholder="Password"></el-input>
        </label>
      </div>

      <div class="signin-button-container">
        <el-button native-type="submi" type="primary" plain>Sign In</el-button>
      </div>

      <div class="signup-tip">
        Do not have account? <router-link to="/signup">Sign Up</router-link>
      </div>

    </form>
  </div>
</template>

<script>
  import axios from 'axios'
  import router from '@/router'

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
        try {
          const response = await axios.post('/api/signin', {
            email: this.email,
            password: this.password
          })
          window.localStorage.setItem('jwt', response.headers.jwt)
          axios.defaults.headers.common['jwt'] = response.headers.jwt
          this.$message({
            message: 'Login sccuess!',
            type: 'success'
          })
          router.push('/home')
        } catch (error) {
          if (error.response.status === 401) {
            return this.$message({
              message: 'Email or Password not match.',
              type: 'error'
            })
          }
          this.$message.error('Sign In Error.')
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
  }

  .signup-tip {
    margin-top: 10px;
  }

  .signup-tip a {
    color: #409EFE
  }

  .signin-button-container {
    margin-top: 10px;
  }
</style>
