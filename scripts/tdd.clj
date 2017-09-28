(require '[cljs.build.api :as b])
(use '[clojure.java.shell :only [sh]]
     '[io.aviso.ansi])

(b/watch "test"
         {:main 'test.core
          :output-to "test-out/test.js"
          :output-dir "test-out"
          :target :nodejs
          :pretty-print true
          :libs ["src/holothuroidea/**/*"]
          :watch-fn (fn [] (println (bold-green (:out (sh "node" "test-out/test.js")))))})
