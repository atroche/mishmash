{:config {:name :custom-emitter, :description "With a custom emitter!", :order 0}
 :data
 [
  [:node-create [] :map]
  [:node-create [:facts] :map]
  [:transform-enable [:facts] :add-fact [{:io.pedestal.app.messages/topic [:facts], :io.pedestal.app.messages.param/id "", :io.pedestal.app.messages.param/text "", :io.pedestal.app.messages.param/keywords ""}]]
  :break
  [:node-create [:facts "a"] :map]
  [:value [:facts "a"] nil {:text "b", :keywords "c"}]
  :break
  [:node-create [:facts "d"] :map]
  [:value [:facts "d"] nil {:text "e", :keywords "f"}]
 ]}