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


  :break
  [:node-create [:mishmash :facts "a"] :map]
  [:value [:mishmash :facts "a"] nil {:text "b", :keywords "c"}]
  :break
  [:node-create [:mishmash :facts "d"] :map]
  [:value [:mishmash :facts "d"] nil {:text "e", :keywords "f"}]
 ]}