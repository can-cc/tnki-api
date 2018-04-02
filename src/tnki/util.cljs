(ns tnki.util
  (:require [tnki.dao :as dao]
            [cljs.core.async :as async])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  )

(defn get-has-continuous-days [user]
  (let [out (async/chan 1)]
    (go
      (let [yestoday-statistics (async/<! (dao/get-yestoday-statistics user))]
        (async/>! out (or (:continuous_days yestoday-statistics) 0))

        ))
    out)
  )

(defn handle-today-finish [user]
  (go
    (let [has-continuous-days (async/<! (get-has-continuous-days user))]
      (dao/record-today-statistics-continuous-days (inc has-continuous-days))
      )
    )
  )
