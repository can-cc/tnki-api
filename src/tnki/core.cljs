(ns tnki.core
  (:require [cljs.nodejs :as nodejs]
            [clojure.string :as string]
            [cljs-time.core :as cljstime]
            [cljs-time.coerce :as cljstime-coerce]))

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

(. app (post "/api/cards"
            (fn [req res]
              (let [body (.-body req)
                    front-text (.-front body)
                    back-text (.-back body)]
                (-> (knex "card")
                    (.insert (clj->js {:front_text front-text
                                       :back_text back-text
                                       :created_at (js/Date.)}))
                    (.returning "*")
                    (.then (fn [results]
                             (println "post card" front-text)
                             (.status res 204)
                             (.send res))))))))

(. app (get "/api/cards"
            (fn [req res]
              (-> (knex "learning_card")
                  (.count "id as count")
                  (.where "next_learn_date" "<" (.getTime (js/Date.)))
                  (.then (fn [results]
                           (let [count (:count (js->clj (first results) :keywordize-keys true))
                                 max-learn-limit 20
                                 all-learn-circle 0]
                             (when (< count 20)
                               (-> (knex "card")
                                   (.select "*")
                                   (.where "learn_time" ">=" (str all-learn-circle))
                                   (.then (fn [results]
                                            ()
                                            (mapv
                                             (fn [card]
                                               (-> (knex "learning_card")
                                                   (.insert (clj->js {:card_id (:id card)
                                                                      :next_learn_date (cljstime-coerce/to-long (cljstime/today))
                                                                      :created_at (js/Date.)
                                                                      }))
                                                   ))
                                             (js->clj results :keywordize-keys true))
                                            ))
                                   (.then (fn [result]
                                            (println result)))

                                   )
                               )

                             )))
                  )
              )))

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
