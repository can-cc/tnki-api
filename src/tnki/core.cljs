(ns tnki.core
  (:require [cljs.nodejs :as nodejs]
            [clojure.string :as string]))

(nodejs/enable-util-print!)

(defonce express (nodejs/require "express"))
(defonce body-parser (nodejs/require "body-parser"))
(defonce http (nodejs/require "http"))
(defonce fs (nodejs/require "fs"))
(defonce path (nodejs/require "path"))
(defonce bcrypt (nodejs/require "bcryptjs"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "/Users/chchen/Tnki/db.sqlite3"}
                     :useNullAsDefault true})))


(def app (express))
(.use app (.json body-parser))

(. app (get "/api/hello"
            (fn [req res] (. res (send "Hello world")))))

(. app (post "/api/signin"
             (fn [req res]
               (let [body (.-body req)
                     email (.-email body)
                     password (.-password body)]

                 (-> (knex "user")
                     (.select "*")
                     (.where (clj->js {:email email}))
                     (.then (fn [results]
                              (if (not result)
                                (do
                                  (.status res 401)
                                  (.send res))
                                (.compareSync bcrypt password (:password (js->clj (first results) :keywordize-keys true))))

                              )))
                 ))))

(. app (post "/api/signup"
             (fn [req res]
               (let [body (.-body req)
                     email (.-email body)
                     password (.-password body)
                     salt (.genSaltSync bcrypt 10)
                     password-hash (.hashSync bcrypt password salt)]
                 (-> (knex "user")
                      (.insert (clj->js {:email email
                                         :password password-hash
                                         :created_at (js/Date.)}))
                      (.returning "*")
                      (.then (fn [ids]
                               (println "signup email:" email (js/Date.))
                               (.json res (clj->js {:email email})))))

                 ))))

(. app (get "/api/cards"
            (fn [req res] (. res (send "Hello world")))))

(. app (post "/api/cards"
             (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards/:id/memory"
             (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards/export"
             (fn [req res] (. res (send "Hello world")))))

(. app (post "/cards/inport"
             (fn [req res] (. res (send "Hello world")))))

(defn -main [& args]
  (doto (.createServer http #(app %1 %2))
    (.listen 3000))
  (println "server listen on http://localhost:3000"))

(set! *main-cli-fn* -main)
