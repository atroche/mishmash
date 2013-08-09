{:config {:name :second-todo, :description "a", :order 0}
 :data
 [
  [:node-create [] :map]
  [:node-create [:main] :map]
  [:node-create [:main :todos] :map]
  [:value [:main :todos] nil []]
  [:transform-enable [:main :todos] :add-todo [{:io.pedestal.app.messages/topic [:todos], :io.pedestal.app.messages.param/text {}}]]
  :break
  [:value [:main :todos] [] ["a"]]
  :break
  [:value [:main :todos] ["a"] ["a" "c"]]
  :break
  [:value [:main :todos] ["a" "c"] ["a" "c" "e"]]
 ]}