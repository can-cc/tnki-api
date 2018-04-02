(ns tnki.middle
  (:require [clojure.string :as string]
            [cljs.core.async :as async]
            [cljs.nodejs :as nodejs]
            [tnki.dao :as dao]
            [tnki.auth :as auth]
            [cljs-time.core :as cljstime]
            [cljs-time.coerce :as cljstime-coerce])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defonce jwt (nodejs/require "jsonwebtoken"))
(defonce moment (nodejs/require "moment"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "./db.sqlite3"}
                     :useNullAsDefault true})))

(defn auth-jwt [req res next]
  (let [jwt-token (.header req "jwt")]
    (if (not jwt-token)
      (do
        (.status res 401)
        (.send res))
      (.verify jwt jwt-token auth/sercet-key
               (fn [err decoded]
                 (if err
                   (do
                     (println err)
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
