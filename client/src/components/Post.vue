<template>
  <div class="post-page">

    <h1>
      <i class="el-icon-edit"></i>
      Add Card:
    </h1>

    <form v-on:submit="post($event)">
      <div class="post-input">
        <div>

        </div>

        <el-card :body-style="{padding: '0'}">
          <h2>
            Front:
            <small>(markdown)</small>
          </h2>
          <div class="input-area">
            <codemirror v-model="inputFront" @input="updateFront" :options="editorOptions"></codemirror>
            <div class="input-preview" v-html="compiledFrontMarkdown"></div>
          </div>
        </el-card>

        <el-card :body-style="{padding: '0'}">
          <h2>
            Back:
            <small>(markdown)</small>
          </h2>
          <div class="input-area">
            <codemirror v-model="inputBackend" @input="updateBackend" :options="editorOptions"></codemirror>
            <div class="input-preview" v-html="compiledBackendMarkdown"></div>
          </div>
        </el-card>

      </div>


      <div class="button-container">
        <el-button native-type="submit" type="primary" plain>Add Card</el-button>
      </div>

    </form>
  </div>
</template>

<script>
  import marked from 'marked'
  import _ from 'lodash'
  import axios from 'axios'
  import { codemirror } from 'vue-codemirror'
  import 'highlight.js/styles/brown-paper.css'

  function mark (md) {
    return marked(md, {
      sanitize: true,
      gfm: true,
      highlight: function (code) {
        return require('highlight.js').highlightAuto(code).value
      }
    })
  }

  export default {
    name: 'PostPage',
    components: {codemirror},
    data () {
      return {
        inputFront: '',
        inputBackend: '',
        frontMarkdown: '',
        backendMarkdown: '',
        editorOptions: {
          mode: 'text/x-markdown',
          theme: 'base16-light',
          lineNumbers: false,
          matchBrackets: true,
          lineWrapping: true,
          extraKeys: {'Enter': 'newlineAndIndentContinueMarkdownList'}
        }
      }
    },
    computed: {
      compiledFrontMarkdown: function () {
        return mark(this.frontMarkdown)
      },
      compiledBackendMarkdown: function () {
        return mark(this.backendMarkdown)
      }
    },
    methods: {
      reset () {
        this.inputFront = ''
        this.inputBackend = ''
        this.frontMarkdown = ''
        this.backendMarkdown = ''
      },
      async post (event) {
        event.preventDefault()
        if (!this.inputFront.trim() || !this.inputBackend.trim()) {
          return this.$message.warning('Please fill card front and back.')
        }
        try {
          event.preventDefault()
          await axios.post('/api/cards', {
            front: this.inputFront,
            back: this.inputBackend
          })
          this.$message.success('Add card successful!')
          this.reset()
        } catch (error) {
          this.$message.error('Add card unknown error.')
        }
      },
      updateFront: _.debounce(function (e) {
        this.frontMarkdown = e
      }, 300),
      updateBackend: _.debounce(function (e) {
        this.backendMarkdown = e
      }, 300)
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @media (min-width: 600px) {
    .post-page {
      padding: 0 20px;
    }
  }

  h1 {
    text-align: left;
    color: #409EFF;
    margin: 20px 0 0 5px;
    font-size: 1.3rem;
  }

  h2 {
    text-align: left;
    color: #409EFF;
    margin: 0 0 0 5px;
    font-size: 1.2rem;
  }

  .el-card {
    margin-top: 20px;
  }

  .input-area {
    display: flex;
    position: relative;
    background-color: #edfcff;
    overflow: hidden;
    height: 250px;
    max-width: 800px;
    margin: 0 auto;
    border-radius: 2px;
    color: #2D2F33;
    text-align: left;
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
    padding: 0 10px;
    width: 50%;
    font-size: 13px;
    text-align: left;
    line-height: 1;
    overflow: auto;
  }

  .button-container {
    margin-top: 20px;
    padding-left: 10px;
    text-align: left;
    padding-bottom: 50px;
  }

</style>
<style>
  .post-page .CodeMirror {
    width: 50%;
    height: 100%;
  }
  .post-page .input-preview pre {
    border: 1px solid #ddd;
    border-radius: 5px;
    padding: 5px;
  }
</style>
