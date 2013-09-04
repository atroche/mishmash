{:config {:name :initial, :description "Initialise app", :order 0}
 :data
 [
  :break
  [:node-create [] :map]
  [:node-create [:mishmash] :map]

  [:node-create [:mishmash :screen-name] :map]
  [:value [:mishmash :screen-name] nil "alistair"]
  :break


  :break
  [:node-create [:mishmash :facts] :map]

  [:transform-enable [:mishmash :facts] :add-fact [{:io.pedestal.app.messages/topic [:facts], :io.pedestal.app.messages.param/id "", :io.pedestal.app.messages.param/text "", :io.pedestal.app.messages.param/keywords "", :io.pedestal.app.messages.param/source "", :io.pedestal.app.messages.param/source-url "", :io.pedestal.app.messages.param/date "", :io.pedestal.app.messages.param/screen-name ""}]]

  :break
  [:node-create [:mishmash :facts "a"] :map]
  [:value [:mishmash :facts "a"] nil {:text "b", :keywords "c"}]
  :break
  [:node-create [:mishmash :facts "d"] :map]
  [:value [:mishmash :facts "d"] nil {:text "e", :keywords "f"}]
 ]}