(ns ^:shared mishmash.effect
    (:require [clojure.string :as string]
              [clojure.data :as data]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app.util.log :as log]
              [io.pedestal.app :as app]))

(defn persist-new-facts [facts]
  (let [new-facts (filter #(nil? (:_id %)) (vals facts))]
    (for [fact new-facts]
      {msg/type :new-fact msg/topic [:facts] :value fact})))

