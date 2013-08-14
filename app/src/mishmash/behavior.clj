(ns ^:shared mishmash.behavior
    (:require [clojure.string :as string]
              [clojure.data :as data]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app.util.log :as log]
              [io.pedestal.app :as app]))


; transforms: handles state transitions

(defn swap-transform [_ message]
  (:value message))


(defn add-fact-transform [old-value message]
  (conj (or old-value []) {:id (:id message)
                           :text (:text message)
                           :keywords (:keywords message)
                           :source (:source message)
                           :source-url (:source-url message)
                           :date (:date message)}))


(defn init-facts-transform [old-value message]
  (:facts message))


; an emitter function
(defn init-main [_]
  [[:transform-enable [:facts] :add-fact [{msg/topic [:facts]
                                           (msg/param :id) ""
                                           (msg/param :text) ""
                                           (msg/param :keywords) ""
                                           (msg/param :source) ""
                                           (msg/param :source-url) ""
                                           (msg/param :date) ""}]]])




(defmulti facts-emitter #(get-in % [:message msg/type]))

(defmethod facts-emitter :add-fact [{:keys [new-model]}]
  (let [fact (last (:facts new-model))]
    [[:value [:facts (:id fact)] fact]]))

(defmethod facts-emitter :initialise-facts [{:keys [old-model new-model] :as inputs}]
  (let [[_ new-facts _ :as diffed] (data/diff old-model new-model)]
    (vec (for [fact (:facts new-facts)]
      [:value [:facts (:id fact)] fact]))))

(defmethod facts-emitter :default [])

; So my data model will just be a vector of facts under :facts
; But my app model will be like {:facts {:xyz {:text "asdf"} :abc {:text "a"}}}
; So that the renderer can be have a separate template for every fact
; And deal with their paths intelligently
; This means we need a custom emitter. Yeah boys.

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
               [:swap [:**] swap-transform]]})

