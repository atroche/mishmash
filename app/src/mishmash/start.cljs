(ns mishmash.start
  (:require [cljs.reader :refer [read-string]]
            [io.pedestal.app.protocols :as p]
            [io.pedestal.app :as app]
            [io.pedestal.app.render.push :as push-render]
            [io.pedestal.app.render :as render]
            [io.pedestal.app.messages :as msg]
            [io.pedestal.app.util.log :as log]
            [mishmash.behavior :as behavior]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close!]]
            [mishmash.rendering :as rendering])
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))



(defn GET [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! ch res)
                      (close! ch)))))
    ch))

(defn POST [url data]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! ch res)
                      (close! ch))))
              "POST"
              data)
    ch))


(defn timestamp-to-date [timestamp]
  (let [date (js/Date. timestamp)]
    (.log js/console timestamp)
    (.log js/console date)
    date))


(defn fetch-facts [input-queue]
  (go
    (let [fact-vec (read-string (<! (GET "/facts")))
          fact-map (into {} (for [fact fact-vec]
                              [(:_id fact) fact]))]
      (p/put-message input-queue
                     {msg/type :initialise-facts
                      msg/topic [:facts]
                      :facts fact-map}))))


(defn fetch-screen-name [input-queue]
  (go
    (let [screen-name (read-string (<! (GET "/screen-name")))]
      (p/put-message input-queue
                     {msg/type :set-screen-name
                      msg/topic [:screen-name]
                      :screen-name (or screen-name "")}))))


(defn services-fn [message input-queue]
  (let [fact (:value message)
        fact (assoc fact :_id (:id fact))]
    (go
      (let [returned-fact (read-string (<! (POST "/facts" (pr-str fact))))]
        (p/put-message input-queue {msg/type :set-fact-as-persisted
                                    msg/topic [:facts]
                                    :id (:id fact)
                                    :timestamp (:timestamp returned-fact)})))))


(defn create-app [render-config]
  (let [app (app/build behavior/dataflow)
        render-fn (push-render/renderer "content" render-config render/log-fn)
        app-model (render/consume-app-model app render-fn)]
    (app/begin app)
    {:app app :app-model app-model}))


(defn ^:export main []
  (let [app (create-app (rendering/render-config))]
    (fetch-facts (:input (:app app)))
    (fetch-screen-name (:input (:app app)))
    (app/consume-effects (:app app) services-fn)))
