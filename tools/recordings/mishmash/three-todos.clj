{:config {:name :three-todos, :description "Entering three todos", :order 0}
 :data
 [
  [:node-create [] :map]
  [:node-create [:todos] :map]
  [:value [:todos] nil []]

  :break
  [:node-create [:todos :a] :map]
  [:value [:todos :a] nil {:datum "number 1"}]
  :break
  [:node-create [:todos :b] :map]
  [:value [:todos :b] nil {:datum "number 2"}]
  :break
 ]}