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



(defn fetch-facts [input-queue]
  (go
    (let [facts (read-string (<! (GET "/facts")))]
      (p/put-message input-queue
                     {msg/type :initialise-facts
                      msg/topic [:facts]
                      :facts facts}))))

(defn services-fn [message input-queue]
  (.log js/console (str "ACTUALLY Sending message to server: " message)))


(defn create-app [render-config]
  (let [app (app/build behavior/example-app)
        render-fn (push-render/renderer "content" render-config render/log-fn)
        app-model (render/consume-app-model app render-fn)]
    (app/begin app)
    {:app app :app-model app-model}))


(defn ^:export main []
  (let [app (create-app (rendering/render-config))]
    (fetch-facts (:input app))
    (app/consume-effects (:app app) services-fn)))
