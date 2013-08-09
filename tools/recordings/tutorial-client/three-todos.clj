{:config {:name :three-todos, :description "Entering three todos", :order 0}
 :data
 [
  [:node-create [] :map]
  [:node-create [:todos] :map]
  [:value [:todos] nil []]
  [:transform-enable [:todos] :add-todo [{:io.pedestal.app.messages/topic [:todos], :io.pedestal.app.messages.param/text {}}]]
  :break
  [:value [:todos] [] ["a"]]
  :break
  [:value [:todos] ["a"] ["a" "b"]]
  :break
  [:value [:todos] ["a" "b"] ["a" "b" "c"]]
 ]}