(ns ^:shared mishmash.behavior
    (:require [clojure.string :as string]
              [clojure.data :as data]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app.util.log :as log]
              [io.pedestal.app :as app]))


; transforms: handles state transitions

(defn add-fact-transform [old-value message]
  (assoc (or old-value {})
         (:id message)
         {:id (:id message)
          :text (:text message)
          :keywords (:keywords message)
          :source (:source message)
          :source-url (:source-url message)
          :date (:date message)}))


(defn init-facts-transform [old-value message]
  (:facts message))

(defn set-fact-as-persisted [old-value message]
  (let [fact-id (:id message)]
    (assoc-in old-value [fact-id :_id] fact-id)))


; an emitter function
(defn init-main [_]
  [[:transform-enable [:mishmash :facts] :add-fact [{msg/topic [:facts]
                                           (msg/param :id) ""
                                           (msg/param :text) ""
                                           (msg/param :keywords) ""
                                           (msg/param :source) ""
                                           (msg/param :source-url) ""
                                           (msg/param :date) ""}]]])




(defmulti facts-emitter #(get-in % [:message msg/type]))

(defmethod facts-emitter :add-fact [{:keys [message]}]
  [[:value
    [:mishmash :facts (:id message)]
    (into {}
      (for [property [:id :text :keywords :source :source-url :date]]
        [property (property message)]))]])

(defmethod facts-emitter :initialise-facts [{:keys [message]}]
  (for [[id fact] (:facts message)]
    [:value [:mishmash :facts id] fact]))

(defmethod facts-emitter :set-fact-as-persisted [{:keys [old-model message] :as input}]
  (let [fact-id (:id message)
        fact (get-in old-model [:facts fact-id])]
    [[:value [:mishmash :facts (:id message)] (assoc fact :_id fact-id)]]))

(defmethod facts-emitter :default [{:keys [new-model]}] [])


; effect functions

(defn persist-new-facts [facts]
  (let [new-facts (filter #(nil? (:_id %)) (vals facts))]
    (for [fact new-facts]
      {msg/type :new-fact msg/topic [:facts] :value fact})))




; dataflow description


(def example-app
  {:version 2
   :debug true
   ; the vector below specifies which function to call when a certain
   ; message is received. [type topic function]
   ; so it matches a message like: {msg/type :inc msg/topic [:my-counter]}
   :emit [{:init init-main}
          [#{[:facts]} facts-emitter]]
   :transform [[:initialise-facts [:facts] init-facts-transform]
               [:add-fact [:facts] add-fact-transform]
               [:set-fact-as-persisted [:facts] set-fact-as-persisted]]
   :effect #{[#{[:facts]} persist-new-facts :single-val]}})

