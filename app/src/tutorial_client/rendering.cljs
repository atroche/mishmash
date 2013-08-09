(ns tutorial-client.rendering
  (:require [domina :as dom]
            [io.pedestal.app.render.push :as render]
            [io.pedestal.app.render.push.templates :as templates]
            [io.pedestal.app.render.push.handlers :as h]
            [io.pedestal.app.messages :as msg]
            [io.pedestal.app.render.events :as events]
            [io.pedestal.app.render.push.handlers.automatic :as d])
  (:use [domina.css :only [sel]]
        [domina.events :only [listen! prevent-default]])
  (:require-macros [tutorial-client.html-templates :as html-templates]))

(def templates (html-templates/tutorial-client-templates))

(defn clear-form []
  (let [inputs (dom/nodes (sel "#datum-form input,textarea"))]
    (.log js/console inputs)
    (doseq [input inputs]
      (dom/set-value! input ""))))

(defn collect-todo-text [renderer [_ path transform-name messages] input-queue]
  (let [todo-text-box (dom/by-id "datum")
        generate-todo-msg (fn [_]
                            (let [todo-text (dom/value todo-text-box)
                                  keywords (dom/value (dom/by-id "keywords"))]
                              (clear-form)
                              (msg/fill transform-name
                                        messages
                                        {:datum {:datum todo-text
                                                 :keywords keywords}})))]
    (events/send-on :click "add-datum" input-queue generate-todo-msg)))



(defn render-template [template-name]
  (fn [renderer [_ path] _]
    (let [parent (render/get-parent-id renderer path)
          id (render/new-id! renderer path)
          html (templates/add-template renderer path (template-name templates))]
      (dom/append! (dom/by-id parent) (html {:id id})))))




(defn update-todo [renderer [_ path old-value new-value] input-queue]
  (templates/update-t renderer path (or new-value {:datum ""})))


(defn render-config []
  [[:node-create [:todos] (render-template :datum-list)]
   [:node-create [:todos :*] (render-template :datum)]

   [:value [:todos :*] update-todo]

   [:node-destroy   [:**] h/default-destroy]
   [:transform-enable [:todos] collect-todo-text]
   [:transform-disable [:todos] (h/remove-send-on-click "add-datum")]])