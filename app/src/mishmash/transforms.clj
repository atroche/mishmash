(ns ^:shared mishmash.transforms
    (:require [clojure.string :as string]
              [clojure.data :as data]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app.util.log :as log]
              [io.pedestal.app :as app]))



(defn add-fact-transform [old-value message]
  (assoc (or old-value {})
         (:id message)
         {:id (:id message)
          :text (:text message)
          :keywords (:keywords message)
          :source (:source message)
          :source-url (:source-url message)
          :date (:date message)
          :screen-name (:screen-name message)}))



(defn init-facts-transform [old-value message]
  (let [facts (:facts message)
        get-time (fn [[id fact]] (:timestamp fact))
        compare-facts (fn [key1 key2]
                        (compare (:timestamp (key1 facts))
                                 (:timestamp (key2 facts))))
        sorted-facts-map (sorted-map-by compare-facts)]
    (into sorted-facts-map facts)))


(defn set-fact-as-persisted [old-value message]
  (let [fact-id (:id message)]
    (-> old-value
      (assoc-in [fact-id :_id] fact-id)
      (assoc-in [fact-id :timestamp] (:timestamp message)))))


(defn set-screen-name [old-value message]
  (:screen-name message))
