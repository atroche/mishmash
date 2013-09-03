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
          :date (:date message)}))


(defn init-facts-transform [old-value message]
  (:facts message))

(defn set-fact-as-persisted [old-value message]
  (let [fact-id (:id message)]
    (assoc-in old-value [fact-id :_id] fact-id)))


(defn set-screen-name [old-value message]
  (:screen-name message))