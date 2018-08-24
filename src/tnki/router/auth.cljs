(ns tnki.router.auth
  (:require [tnki.dao :as dao]
            [tnki.middle :as middle]
            [tnki.auth :as auth]
            [cljs.core.async :as async]
            [cljs.nodejs :as nodejs]
            [clojure.string :as string]
            [cljs-time.core :as cljstime]
            [cljs-time.coerce :as cljstime-coerce])
  )


(nodejs/enable-util-print!)

(defonce express (nodejs/require "express"))
(defonce jwt (nodejs/require "jsonwebtoken"))
(defonce bcrypt (nodejs/require "bcryptjs"))
(def knex ((nodejs/require "knex")
           (clj->js {:client "sqlite3"
                     :connection {:filename "./db.sqlite3"}
                     :useNullAsDefault true})))


(def router (.Router express))

(. router (post "/api/signin"
                (fn [req res]
                  (let [body (.-body req)
                        email (.-email body)
                        password (.-password body)]
                    (-> (knex "user")
                        (.select "*")
                        (.where (clj->js {:email email}))
                        (.then (fn [results]
                                 (if (= 0 (count results))
                                   (do
                                     (.status res 401)
                                     (.send res))
                                   (if (.compareSync bcrypt password (:password (js->clj (first results) :keywordize-keys true)))
                                     (let [result (first results)
                                           id (.-id (first results))
                                           jwt (.sign jwt
                                                      (clj->js {:data {:user {:email email :id id}}
                                                                :exp (+ (/ (js/Date.) 1000) (* 300 60 60))})
                                                      auth/sercet-key)]
                                       (.header res "jwt" jwt)
                                       (.header res "user-id" id) ;; header not case sensitive
                                       (.send res (clj->js {:email email})))
                                     (do
                                       (.status res 401)
                                       (.send res)))))))))))

(. router (post "/api/signup"
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
