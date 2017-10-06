<template>
  <div class="post-page">

    <form v-on:submit="post($event)">
      <div class="post-input">
        <h2>
          Front:
        </h2>
        <div>

        </div>
        <div class="input-area">
          <textarea :value="inputFront" @input="updateFront"></textarea>
          <div class="input-preview" v-html="compiledFrontMarkdown"></div>
        </div>
      </div>

      <div class="post-input">
        <h2>
          Backend:
        </h2>
        <div class="input-area">
          <textarea :value="inputBackend" @input="updateBackend"></textarea>
          <div class="input-preview" v-html="compiledBackendMarkdown"></div>
        </div>
      </div>


      <div>
        <Button type="submit">Post</Button>
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
      async post (event) {
        event.preventDefault()
        await axios.post('/api/cards', {
          front: this.inputFront,
          back: this.inputBackend
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
    position: relative;
    background-color: #e8e8e8;
    overflow: hidden;
    border: 1px solid #e8e8e8;
    height: 250px;
    max-width: 800px;
    margin: 0 auto;
    border-radius: 2px;
  }

  .input-area textarea {
    box-shadow: 0 0 5px #999;
    border: 0;
    resize: none;
    outline: none;
    width: 50%;
    display: block;
    font-size: 1.1rem;
  }

  .input-preview {
    box-sizing: border-box;
    padding-left: 10px;
    width: 50%;
    font-size: 12px;
    text-align: left;
  }
</style>
