(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app :as app]))


; transforms: handles state transitions

(defn swap-transform [_ message]
  (:value message))


(defn add-todo-transform [old-value message]
  (conj old-value {:id (:id message)
                   :text (:text message)}))




; an emitter function
(defn init-main [_]
  [[:transform-enable [:todos] :add-todo [{msg/topic [:todos]
                                           (msg/param :id) ""
                                           (msg/param :text) ""}]]])




; dataflow description

(def example-app
  {:version 2
   :debug true
   ; the vector below specifies which function to call when a certain
   ; message is received. [type topic function]
   ; so it matches a message like: {msg/type :inc msg/topic [:my-counter]}
   :emit [{:init init-main}
          [#{[:*]} (app/default-emitter [])]]
   :transform [[:add-todo [:todos] add-todo-transform]
               [:swap [:**] swap-transform]]})

