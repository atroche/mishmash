(ns ^:shared mishmash.behavior
    (:require [clojure.string :as string]
              [clojure.data :as data]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app.util.log :as log]
              [io.pedestal.app :as app]
              [mishmash.transforms :as transforms]
              [mishmash.effect :as effect]
              [mishmash.emitters :as emitters]))


; dataflow description


(def dataflow
  {:version 2
   :debug true
   ; the vector below specifies which function to call when a certain
   ; message is received. [type topic function]
   ; so it matches a message like: {msg/type :inc msg/topic [:my-counter]}
   :emit [{:init emitters/init-main}
          [#{[:facts]} emitters/facts-emitter]]
   :transform [[:initialise-facts [:facts] transforms/init-facts-transform]
               [:add-fact [:facts] transforms/add-fact-transform]
               [:set-fact-as-persisted [:facts] transforms/set-fact-as-persisted]]
   :effect #{[#{[:facts]} effect/persist-new-facts :single-val]}})

