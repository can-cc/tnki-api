(ns tnki.router.file
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
(defonce multer (nodejs/require "multer"))

(def upload (multer (clj->js {:dest "uploads/"})))

(def router (.Router express))

(. router (post "/api/image"
                (.single upload "image")
                (fn [req res]
                  (let [file (.-file req)]
                    (.send res (clj->js {:file file})))
                  )))
