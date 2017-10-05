<template>
  <div class="post-page">

    <form>
      <div class="post-input">
        <div>
          Front:
        </div>
        <div>

        </div>
        <div class="input-area">
          <textarea :value="inputFront" @input="updateFront"></textarea>
          <div class="input-preview" v-html="compiledFrontMarkdown"></div>
        </div>
      </div>

      <div class="post-input">
        <div>
          Backend:
        </div>
        <div class="input-area">
          <textarea :value="inputBackend" @input="updateBackend"></textarea>
          <div class="input-preview" v-html="compiledBackendMarkdown"></div>
        </div>
      </div>


    </form>
  </div>
</template>

<script>
  import marked from 'marked'
  import _ from 'lodash'
  import axios from 'axios'

  export default {
    name: 'PostPage',
    data () {
      return {
        inputFront: '# input card frontend',
        inputBackend: '## input card backend'
      }
    },
    computed: {
      compiledFrontMarkdown: function () {
        return marked(this.inputFront, { sanitize: true })
      },
      compiledBackendMarkdown: function () {
        return marked(this.inputBackend, { sanitize: true })
      }
    },
    methods: {
      async signIn (event) {
        event.preventDefault()
        await axios.post('/api/signin', {
          email: this.email,
          password: this.password
        })
      },
      updateFront: _.debounce(function (e) {
        this.inputFront = e.target.value
      }, 300),
      updateBackend: _.debounce(function (e) {
        this.inputBackend = e.target.value
      }, 300)
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .post-input {

  }

  .input-area {
    display: flex;
  }

  .input-area textarea {
    box-shadow: 0 0 5px #999;
    border: 0;
    resize: none;
    outline: none;
  }

  .input-preview {

  }
</style>
