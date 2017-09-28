(ns tnki.core
  (:require [cljs.nodejs :as nodejs]
            [clojure.string :as string]))

(nodejs/enable-util-print!)

(defonce express (nodejs/require "express"))
(defonce http (nodejs/require "http"))

(def app (express))

(. app (get "/hello"
            (fn [req res] (. res (send "Hello world")))))

(. app (get "/cards"
            (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards"
             (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards/:id/memory"
             (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards/export"
             (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards/inport"
             (fn [req res] (. res (send "Hello world")))))

(defn -main [& args]
  (doto (.createServer http #(app %1 %2))
    (.listen 3000)))

(set! *main-cli-fn* -main)
