(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
           {:main 'tnki.core
            :asset-path "out"
            :output-to "out/tnki-server.js"
            :output-dir "out"
            :optimizations :simple
            :target :nodejs
            :verbose true
            :cache-analysis true
            :pretty-print true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
x
