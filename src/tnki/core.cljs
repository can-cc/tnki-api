(ns tnki.core
  (:require [tnki.dao :as dao]
            [tnki.middle :as middle]
            [cljs.core.async :as async]
            [cljs.nodejs :as nodejs]
            [clojure.string :as string]
            [cljs-time.core :as cljstime]
            [cljs-time.coerce :as cljstime-coerce])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(nodejs/enable-util-print!)

(def sercet-key "sercet")

(defonce express (nodejs/require "express"))
(defonce body-parser (nodejs/require "body-parser"))
(defonce http (nodejs/require "http"))
(defonce https (nodejs/require "https"))
(defonce fs (nodejs/require "fs"))
(defonce path (nodejs/require "path"))
(defonce bcrypt (nodejs/require "bcryptjs"))
(defonce jwt (nodejs/require "jsonwebtoken"))
(defonce moment (nodejs/require "moment"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "./db.sqlite3"}
                     :useNullAsDefault true})))

(def app (express))
(.use app (.json body-parser))

(defn auth-jwt [req res next]
  (let [jwt-token (.header req "jwt")]
    (if (not jwt)
      (do
        (.status res 401)
        (.send res))
      (.verify jwt jwt-token sercet-key
               (fn [err decoded]
                 (if err
                   (do
                     (.status res 401)
                     (.send res))
                   (let [jwt-data (js->clj decoded :keywordize-keys true)]
                     (aset req "user" (:user (:data jwt-data)))
                     (next))))))))

(defn insure-today-statistics [req res next]
  (let [user (.-user req)
        email (:email user)]
    (-> (knex "user_daily_statistics")
        (.count "user_email as count")
        (.where "user_email" "=" email)
        (.andWhere "date" "=" (-> (moment)
                                  (.format "YYYY-MM-DD")))
        (.then (fn [results]
                 (if (> (:count (js->clj (first results) :keywordize-keys true)) 0) 
                   (js/Promise.resolve)
                   (-> (knex "user_daily_statistics")
                       (.insert (clj->js {:user_email email
                                          :date (-> (moment)
                                                    (.format "YYYY-MM-DD"))}))))))
        (.then #(next)))
    ))

(. app (get "/api/hello"
            (fn [req res]
              (.send res "hello world!"))))

(. app (post "/api/signin"
             (fn [req res]
               (let [body (.-body req)
                     email (.-email body)
                     password (.-password body)]
                 (-> (knex "user")
                     (.select "*")
                     (.where (clj->js {:email email}))
                     (.then (fn [results]
                              (if (not results)
                                (do
                                  (.status res 401)
                                  (.send res))
                                (if (.compareSync bcrypt password (:password (js->clj (first results) :keywordize-keys true)))
                                  (let [jwt (.sign jwt
                                                   (clj->js {:data {:user {:email email}}
                                                             :exp (+ (/ (js/Date.) 1000) (* 300 60 60))})
                                                   sercet-key)]
                                    (.header res "jwt" jwt)
                                    (.send res (clj->js {:email email})))
                                  (do
                                    (.status res 401)
                                    (.send res)))))))))))

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
                              (.json res (clj->js {:email email})))))))))

(. app (get "/api/daily/statistics"
           auth-jwt
           insure-today-statistics
           middle/check-pull-card-to-learn
           (fn [req res]
             (go
               (let [user (.-user req)
                     email (:email user)
                     all-finish (async/<! (dao/get-all-finish-card-count-chan user))
                     total-days (async/<! (dao/get-user-total-learn-days-chan user))
                     need-learn-card {:need_learn_count (async/<! (dao/get-user-need-learning-card-count user))}
                     statistics-table-data (async/<! (dao/get-user-daily-statistics user))
                     statistics-data (merge need-learn-card
                                            total-days
                                            all-finish
                                            statistics-table-data)]
                 (.send res (clj->js statistics-data))
                 )))))

(. app (post "/api/cards"
             auth-jwt
             (fn [req res]
               (let [body (.-body req)
                     user (js->clj (.-user req))
                     front-text (.-front body)
                     back-text (.-back body)]
                 (-> (knex "card")
                     (.insert (clj->js {:front_text front-text
                                        :back_text back-text
                                        :created_at (js/Date.)}))
                     (.returning "*")
                     (.then (fn [results]
                              (-> (knex "user_learn_card")
                                  (.insert (clj->js {:user_email (:email user)
                                                     :card_id (first results)
                                                     :learn_time_base 0}))
                                  (.then (fn [results]
                                           (println "post card" front-text (js/Date.))
                                           (.status res 204)
                                           (.send res)))))))))))

(. app (get "/api/cards/:userId"
            auth-jwt
            middle/check-pull-card-to-learn
            (fn [req res]
              (let [
                    user-id (str (.-userId params))
                    user (js->clj (.-user req))]
                (-> (knex "learning_card")
                    (.select "*" "learning_card.id as id")
                    (.innerJoin "user_learn_card" "learning_card.card_id" "user_learn_card.card_id")
                    (.innerJoin "card" "card.id" "learning_card.card_id")
                    (.where "next_learn_date" "<" (.getTime (js/Date.)))
                    (.andWhere "user_email" "=" (:email user))
                    (.then (fn [result]
                             (.send res (clj->js result)))))
                ))))

(. app (get "/api/cards/learn"
            auth-jwt
            middle/check-pull-card-to-learn
            (fn [req res]
              (let [max-learn-limit 20
                    learn_time_base 0
                    user (js->clj (.-user req))]
                (-> (knex "learning_card")
                    (.select "*" "learning_card.id as id")
                    (.innerJoin "user_learn_card" "learning_card.card_id" "user_learn_card.card_id")
                    (.innerJoin "card" "card.id" "learning_card.card_id")
                    (.where "next_learn_date" "<" (.getTime (js/Date.)))
                    (.andWhere "user_email" "=" (:email user))
                    (.then (fn [result]
                             (.send res (clj->js result)))))
                ))))

(. app (post "/api/cards/:id/memory"
             auth-jwt
             (fn [req res]
               (let [body (.-body req)
                     user (.-user req)
                     params (.-params req)
                     learning-card-id (str (.-id params))
                     memory-level (str (.-memoryLevel body))
                     db-memory-level-name (case memory-level
                                            "0" "easy_time"
                                            "1" "remeber_time"
                                            "2" "forget_time"
                                            "forget_time")
                     next-learn-days (case memory-level
                                       "0" 14
                                       "1" 7
                                       "2" 1
                                       1)]
                 (-> (js/Promise.all
                      [(-> (knex "learning_card")
                           (.where "id" "=" learning-card-id)
                           (.increment db-memory-level-name 1)
                           (.update (clj->js {:next_learn_date (-> (moment)
                                                                   (.startOf "day" )
                                                                   (.add next-learn-days "days")
                                                                   (.valueOf))}))
                           )
                       (-> (knex "learning_card")
                           (.where "id" "=" learning-card-id)
                           (.increment db-memory-level-name 1))
                       (-> (knex "user_daily_statistics")
                           (.where "user_email" (:email user))
                           (.andWhere "date" "=" (-> (moment)
                                                     (.format "YYYY-MM-DD")))
                           (.increment "learn_time" 1))
                      (-> (knex "user_daily_statistics")
                           (.where "user_email" (:email user))
                           (.andWhere "date" "=" (-> (moment)
                                                     (.format "YYYY-MM-DD")))
                           (.increment db-memory-level-name 1))
                       ])
                     (.then (fn [result]
                              (.status res 204)
                              (.send res)))
                     )
                 ))))

(defn -main [& args]
  (doto (.createServer http #(app %1 %2))
    (.listen 3000 "127.0.0.1"))
  (doto (.createServer https (clj->js
                              {:key (.readFileSync fs "/etc/letsencrypt/live/tnki.octopuese.xyz/privkey.pem")
                               :cert (.readFileSync fs "/etc/letsencrypt/live/tnki.octopuese.xyz/cert.pem")})
                       app)
    (.listen 3001 "127.0.0.1"))
  (println "server listen on http://localhost:3000"))

(set! *main-cli-fn* -main)
