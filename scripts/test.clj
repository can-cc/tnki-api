(require '[cljs.test :as t])
(require '[cljs.build.api :as b])
(use '[clojure.java.shell :only [sh]]
     '[io.aviso.ansi])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "test"
           {:main 'test.core
            :output-to "test-out/test.js"
            :output-dir "test-out"
            :target :nodejs
            :pretty-print true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
