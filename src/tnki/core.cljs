(ns tnki.core
  (:require [tnki.dao :as dao]
            [tnki.middle :as middle]
            [tnki.auth :as auth]
            [tnki.router.auth :as router-auth]
            [cljs.core.async :as async]
            [cljs.nodejs :as nodejs]
            [clojure.string :as string]
            [cljs-time.core :as cljstime]
            [cljs-time.coerce :as cljstime-coerce])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(nodejs/enable-util-print!)

(defonce express (nodejs/require "express"))
(defonce body-parser (nodejs/require "body-parser"))
(defonce http (nodejs/require "http"))
(defonce https (nodejs/require "https"))
(defonce fs (nodejs/require "fs"))
(defonce path (nodejs/require "path"))
(defonce bcrypt (nodejs/require "bcryptjs"))
(defonce moment (nodejs/require "moment"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "./db.sqlite3"}
                     :useNullAsDefault true})))

(def app (express))
(.use app (.json body-parser))

(. app (get "/api/hello"
            (fn [req res]
              (.send res "hello world!"))))


(. app (get "/api/daily/statistics"
            middle/auth-jwt
            middle/insure-today-statistics
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
             middle/auth-jwt
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

(. app (get "/api/cards/user/:userId/learn"
            middle/auth-jwt
            middle/check-pull-card-to-learn
            (fn [req res]
              (let [params (.-params req)
                    user-id (str (.-userId params))
                    user (js->clj (.-user req))]
                (if (not (== user-id (str (:id user))))
                  (-> res
                      (.status 401)
                      (.send))
                  (-> (knex "card")
                      (.select "*")
                      (.innerJoin "user_learn_card" "user_learn_card.card_id" "card.id")
                      (.where "user_learn_card.user_email" "=" (:email user))
                      (.then (fn [result]
                               (.send res (clj->js result)))))
                  )
                ))))

(. app (get "/api/cards/user/:userId/learn/today"
            middle/auth-jwt
            middle/check-pull-card-to-learn
            (fn [req res]
              (let [max-learn-limit 20
                    learn_time_base 0
                    params (.-params req)
                    user-id (str (.-userId params))
                    user (js->clj (.-user req))]
                (if (not (== user-id (str (:id user))))
                  (-> res
                      (.status 401)
                      (.send))
                  (-> (knex "learning_card")
                      (.select "*" "learning_card.id as id")
                      (.innerJoin "user_learn_card" "learning_card.card_id" "user_learn_card.card_id")
                      (.innerJoin "card" "card.id" "learning_card.card_id")
                      (.where "next_learn_date" "<" (.getTime (js/Date.)))
                      (.andWhere "user_email" "=" (:email user))
                      (.then (fn [result]
                               (.send res (clj->js result)))))
                  )
                ))))

(. app (post "/api/cards/:id/memory"
             middle/auth-jwt
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

(.use app router-auth/router)


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
