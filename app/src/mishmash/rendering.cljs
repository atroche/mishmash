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

; TODO: use cljs-uuid library instead
(defn uuid
  "returns a type 4 random UUID: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx"
  []
  (let [r (repeatedly 30 (fn [] (.toString (rand-int 16) 16)))]
    (apply str (concat (take 8 r) ["-"]
                       (take 4 (drop 8 r)) ["-4"]
                       (take 3 (drop 12 r)) ["-"]
                       [(.toString  (bit-or 0x8 (bit-and 0x3 (rand-int 15))) 16)]
                       (take 3 (drop 15 r)) ["-"]
                       (take 12 (drop 18 r))))))


(defn clear-form []
  (let [inputs (dom/nodes (sel "#fact-form input,textarea"))]
    (doseq [input inputs]
      (dom/set-value! input ""))))

(defn collect-fact-data [renderer [_ path transform-name messages] input-queue]
  (.log js/console (dom/single-node (sel "button[name=add-fact]")))
  (let [fact-text-box (dom/by-id "fact")
        generate-fact-msg (fn [_]
                            (let [fact-text (dom/value fact-text-box)
                                  keywords (dom/value (dom/by-id "keywords"))
                                  source (dom/value (dom/by-id "source"))
                                  source-url (dom/value (dom/by-id "source-url"))
                                  date (dom/value (dom/by-id "date"))
                                  ; TODO the following line is just fucked
                                  screen-name (dom/text (dom/single-node (sel "span.screen-name")))]
                              (clear-form)
                              (.focus fact-text-box)
                              (msg/fill transform-name
                                        messages
                                        {:id (uuid)
                                         :text fact-text
                                         :keywords keywords
                                         :source source
                                         :source-url source-url
                                         :date date
                                         :screen-name screen-name})))]
    (events/send-on :click (dom/single-node (sel "button[name=add-fact]")) input-queue generate-fact-msg)))



(defn render-template
  ([template-name]
    (render-template template-name nil))
  ([template-name parent-id]
    (fn [renderer [_ path] _]
      (let [parent (render/get-parent-id renderer path)
            parent-id (or parent-id parent)
            id (render/new-id! renderer path)
            template (template-name templates)
            html (templates/add-template renderer path template)]
        (dom/prepend! (dom/by-id parent-id) (html {:id id}))))))


(defn update-screen-name [renderer [_ path old-value new-value] input-queue]
  (let [template-data {:screen-name new-value}
        template-data (if new-value
                        (assoc template-data :screen-name-style "display:inline;"
                                             :add-fact-style "display:block;"
                                             :sign-in-button-style "display:none;")
                        (assoc template-data :screen-name-style "display:none;"
                                             :add-fact-style "display:none;"
                                             :sign-in-button-style ""))]
    (templates/update-t renderer path template-data)))


(defn update-fact [renderer [_ path old-value new-value] input-queue]
  (templates/update-t renderer path (or new-value {:fact "fact-list"})))



(defn render-config []
  [[:node-create [:mishmash] (render-template :app)]

   [:node-create
    [:mishmash :screen-name]
    (render-template :auth-status
                     "auth-status-container")]

   [:value [:mishmash :screen-name] update-screen-name]

   [:node-create [:mishmash :facts] (render-template :fact-list "fact-list")]
   [:node-create [:mishmash :facts :*] (render-template :fact)]

   [:value [:mishmash :facts :*] update-fact]

   [:node-destroy   [:**] h/default-destroy]
   [:transform-enable [:mishmash :facts] collect-fact-data]
   [:transform-disable [:mishmash :facts] (h/remove-send-on-click "add-fact")]])

