(defproject tnki "0.0.1"
  :url "https://github.com/fwchen/Tnki.git"
  :description "Tnki"
  :dependencies [[com.cemerick/piggieback "0.2.1"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.671"]
                 [cider/cider-nrepl "0.15.0"]
                 [org.clojure/core.async "0.3.426"]
                 [funcool/promesa "1.8.0"]
                 [fipp "0.6.8"]
                 [markdown-clj "0.9.99"]
                 [io.aviso/pretty "0.1.34"]
                 [lein-doo "0.1.7"]
                 [com.andrewmcveigh/cljs-time "0.5.0"]]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :jvm-opts ^:replace ["-Xmx1g"]
  :plugins [[lein-npm "0.7.0-rc1"]
            [io.aviso/pretty "0.1.34"]]
  :npm {:dependencies [[source-map-support "0.4.0"]
                       [colors "1.1.2"]
                       [ora "v1.3.0"]
                       [express "v4.15.5"]
                       [bcryptjs "v2.4.3"]
                       [body-parser "v1.18.2"]
                       [knex "v0.13.0"]]
        :description "tnki"
        :name "tnki",
        :main "out/tnki"
        :repository {:type "git"
                     :url "git@github.com:fwchen/Tnki.git"}
        :package {:bin {:tnki "tnki-server"}}
        :private false
        :license "MIT"}
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target")
