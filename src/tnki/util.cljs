(ns tnki.util
  (:require [tnki.dao :as dao]
            [cljs.core.async :as async])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defn get-has-continuous-days [user]
  (let [yestoday-statistics (async/<! (dao/get-yestoday-statistics user))
        out (async/chan 1)]
    (println yestoday-statistics)
    (go (async/>! out 1))
    out
    )
  )

(defn handle-today-finish [user]
  (let [has-continuous-days (async/<! (get-has-continuous-days user))]))
