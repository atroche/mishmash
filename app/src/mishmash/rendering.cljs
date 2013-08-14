(ns mishmash.rendering
  (:require [domina :as dom]
            [io.pedestal.app.render.push :as render]
            [io.pedestal.app.render.push.templates :as templates]
            [io.pedestal.app.render.push.handlers :as h]
            [io.pedestal.app.messages :as msg]
            [io.pedestal.app.render.events :as events]
            [io.pedestal.app.render.push.handlers.automatic :as d])
  (:use [domina.css :only [sel]]
        [domina.events :only [listen! prevent-default]])
  (:require-macros [mishmash.html-templates :as html-templates]))

(def templates (html-templates/mishmash-templates))

(defn clear-form []
  (let [inputs (dom/nodes (sel "#fact-form input,textarea"))]
    (.log js/console inputs)
    (doseq [input inputs]
      (dom/set-value! input ""))))

(defn collect-fact-data [renderer [_ path transform-name messages] input-queue]
  (let [fact-text-box (dom/by-id "fact")
        generate-fact-msg (fn [_]
                            (let [fact-text (dom/value fact-text-box)
                                  keywords (dom/value (dom/by-id "keywords"))
                                  source (dom/value (dom/by-id "source"))
                                  source-url (dom/value (dom/by-id "source-url"))
                                  date (dom/value (dom/by-id "date"))]
                              (clear-form)
                              (.focus fact-text-box)
                              (msg/fill transform-name
                                        messages
                                        {:id "xyz"
                                         :text fact-text
                                         :keywords keywords
                                         :source source
                                         :source-url source-url
                                         :date date})))]
    (events/send-on :click "add-fact" input-queue generate-fact-msg)))



(defn render-template [template-name]
  (fn [renderer [_ path] _]
    (let [parent (render/get-parent-id renderer path)
          id (render/new-id! renderer path)
          html (templates/add-template renderer path (template-name templates))]
      (dom/append! (dom/by-id parent) (html {:id id})))))




(defn update-fact [renderer [_ path old-value new-value] input-queue]
  (templates/update-t renderer path (or new-value {:fact ""})))


(defn render-config []
  [[:node-create [:facts] (render-template :fact-list)]
   [:node-create [:facts :*] (render-template :fact)]

   [:value [:facts :*] update-fact]

   [:node-destroy   [:**] h/default-destroy]
   [:transform-enable [:facts] collect-fact-data]
   [:transform-disable [:facts] (h/remove-send-on-click "add-fact")]])