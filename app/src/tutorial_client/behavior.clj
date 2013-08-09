(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app :as app]))


; transforms: handles state transitions

(defn add-todo [old-value message]
  (conj (or old-value []) (:datum message)))


; whatever this returns will replace old-value in the data model
(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))



; an emitter function
(defn init-main [_]
  [[:transform-enable [:todos] :add-todo [{msg/topic [:todos] (msg/param :datum) {}}]]])




; dataflow description

(def example-app
  {:version 2
   :debug true
   ; the vector below specifies which function to call when a certain
   ; message is received. [type topic function]
   ; so it matches a message like: {msg/type :inc msg/topic [:my-counter]}
   :transform [[:add-todo [:todos] add-todo]]

   :emit [{:init init-main}
          [#{[:*]} (app/default-emitter [])]]})

