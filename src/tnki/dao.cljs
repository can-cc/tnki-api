(ns tnki.dao
  (:require [cljs.nodejs :as nodejs]
            [clojure.string :as string]
            [cljs.core.async :as async])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defonce fs (nodejs/require "fs"))
(defonce path (nodejs/require "path"))
(defonce moment (nodejs/require "moment"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "./db.sqlite3"}
                     :useNullAsDefault true})))

(defn get-all-finish-card-count-chan [user]
  (let [out (async/chan 1)]
    (-> (knex "learning_card")
        (.count "id as count")
        (.innerJoin "user_learn_card" "learning_card.card_id" "user_learn_card.card_id")
        (.where "user_email" "=" (:email user))
        (.where "learn_time_base" ">" 1)
        (.then (fn [results]
                 (go (async/>! out {:all_finish (:count (js->clj (first results) :keywordize-keys true))})))))
    out))

(defn get-user-total-learn-days-chan [user]
  (let [out (async/chan 1)]
    (-> (knex "user_daily_statistics")
        (.count "user_email as count")
        (.where "user_email" "=" (:email user))
        (.then (fn [results]
                 (go (async/>! out {:total_days (:count (js->clj (first results) :keywordize-keys true))}))))
        )
    out
    ))

(defn get-user-need-learning-card-count [user]
  (let [out (async/chan 1)]
    (-> (knex "learning_card")
        (.count "* as count")
        (.innerJoin "user_learn_card" "learning_card.card_id" "user_learn_card.card_id")
        (.innerJoin "card" "card.id" "learning_card.card_id")
        (.where "next_learn_date" "<" (.getTime (js/Date.)))
        (.andWhere "user_email" "=" (:email user))
        (.then (fn [results]
                 (go (async/>! out (:count (js->clj (first results) :keywordize-keys true))))
                )))
    out
    ))

(defn get-user-daily-statistics [user]
  (let [out (async/chan 1)]
    (-> (knex "user_daily_statistics")
        (.select "*")
        (.where "user_email" "=" (:email user))
        (.andWhere "date" "=" (-> (moment)
                                  (.format "YYYY-MM-DD")))
        (.then (fn [results]
                 (go (async/>! out (first (js->clj results :keywordize-keys true))))
                 )))
    out))
