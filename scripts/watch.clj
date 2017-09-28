(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'tnki.core
          :output-to "out/tnki-server.js"
          :output-dir "out"
          :target :nodejs
          :verbose true
          :foreign-libs [{:file "src"
                          :module-type :es6}] ;; or :commonjs / :amd
          :pretty-print true})
