(ns ^:shared mishmash.emitters
    (:require [clojure.string :as string]
              [clojure.data :as data]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app.util.log :as log]
              [io.pedestal.app :as app]))



(defn init-main [_]
  [[:transform-enable [:mishmash :facts] :add-fact [{msg/topic [:facts]
                                                    (msg/param :id) ""
                                                    (msg/param :text) ""
                                                    (msg/param :keywords) ""
                                                    (msg/param :source) ""
                                                    (msg/param :source-url) ""
                                                    (msg/param :date) ""
                                                    (msg/param :screen-name) ""}]]])




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



(defn screen-name-emitter [{:keys [message]}]
  [[:value [:mishmash :screen-name] (:screen-name message)]])

