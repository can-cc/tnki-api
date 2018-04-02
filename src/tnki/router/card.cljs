(ns tnki.router.card
    (:require [tnki.dao :as dao]
            [tnki.middle :as middle]
            [tnki.auth :as auth]
            [tnki.util :as util]
            [cljs.core.async :as async]
            [cljs.nodejs :as nodejs]
            [clojure.string :as string]
            [cljs-time.core :as cljstime]
            [cljs-time.coerce :as cljstime-coerce])
    (:require-macros
     [cljs.core.async.macros :refer [go go-loop]])
    )


(nodejs/enable-util-print!)

(defonce express (nodejs/require "express"))
(defonce moment (nodejs/require "moment"))
(defonce camelcase-keys (nodejs/require "camelcase-keys"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "./db.sqlite3"}
                     :useNullAsDefault true})))


(def router (.Router express))

(. router (post "/api/cards"
             middle/auth-jwt
             (fn [req res]
               (let [body (.-body req)
                     user (js->clj (.-user req))
                     front-text (.-frontText body)
                     back-text (.-backText body)
                     front-image (.-frontImage body)
                     back-image (.-backImage body)]
                 (-> (knex "card")
                     (.insert (clj->js {:front_text front-text
                                        :front_image front-image
                                        :back_text back-text
                                        :back_image back-image
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

(. router (get "/api/cards/user/:userId"
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
                               (.send res (camelcase-keys (clj->js result))))))
                  )
                ))))

(. router (get "/api/cards/user/:userId/learning"
               middle/auth-jwt
               middle/insure-today-statistics
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
                         (.innerJoin "learning_card" "learning_card.card_id" "card.id")
                         (.where "user_learn_card.user_email" "=" (:email user))
                         (.then (fn [result]
                                  (.send res (camelcase-keys (clj->js result))))))
                     )
                   ))))

(. router (get "/api/cards/user/:userId/learn/today"
            middle/auth-jwt
            middle/insure-today-statistics
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
                               (.send res (camelcase-keys (clj->js result))))))
                  )
                )
              )))

(. router (post "/api/cards/:id/memory"
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
                              (go
                                (let [today-should-learn-card-count (async/<! (dao/get-today-should-learn-card-count user))]
                                  (if (= 0 today-should-learn-card-count)
                                    (util/handle-today-finish user)))
                                )

                              (.status res 204)
                              (.send res)))
                     )
                 ))))
