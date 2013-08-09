(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app :as app]))


; transforms: handles state transitions

(defn swap-transform [_ message]
  (:value message))

; an emitter function
;(defn init-main [_]
;  [[:transform-enable [:todos] :create [{msg/topic [:todos] (msg/param :datum) {}}]]])




; dataflow description

(def example-app
  {:version 2
   :debug true
   ; the vector below specifies which function to call when a certain
   ; message is received. [type topic function]
   ; so it matches a message like: {msg/type :inc msg/topic [:my-counter]}
   :transform [[:swap [:**] swap-transform]]})

