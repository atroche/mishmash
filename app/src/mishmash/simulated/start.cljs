(ns mishmash.simulated.start
  (:require [io.pedestal.app.render.push.handlers.automatic :as d]
            [io.pedestal.app :as app]
            [io.pedestal.app.protocols :as p]
            [io.pedestal.app.messages :as msg]
            [io.pedestal.app.util.log :as log]
            [mishmash.start :as start]
            ;; This needs to be included somewhere in order for the
            ;; tools to work.
            [io.pedestal.app-tools.tooling :as tooling]))

(defn services-fn [message input-queue]
  (let [facts (:value message)
        new? (fn [fact] (nil? (:_id fact)))
        new-facts (filter new? facts)]
    (doseq [fact new-facts]
      (log/info "persisting fact")
      (p/put-message input-queue {msg/type :set-fact-as-persisted
                                  msg/topic [:facts]
                                  :id (:id fact)}))))
    ; for each new fact
      ; post to server
      ; send message to say that it's been persisted
    ; (.log js/console (pr-str new-facts))))



(defn ^:export main []
  (let [app (start/create-app d/data-renderer-config)]
    (p/put-message (:input (:app app))
                   {msg/type :initialise-facts
                    msg/topic [:facts]
                    :facts [{:_id "1" :id "1" :text "asdf"} {:_id "2" :id "2" :text "wah"}]})
    (p/put-message (:input (:app app))
                   {msg/type :add-fact
                    msg/topic [:facts]
                    :id "100"
                    :text "coolness"})
    (app/consume-effects (:app app) services-fn)))


; generate fact id clientside
; persist to server, get back _id as confirmation
; set _id of fact, so we know it's been persisted