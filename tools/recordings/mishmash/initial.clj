{:config {:name :initial, :description "Initialise app", :order 0}
 :data
 [
  :break
  [:node-create [] :map]
  [:node-create [:mishmash] :map]


  [:value [:mishmash] nil {}]
  :break
  [:value [:mishmash] {} {:screen-name "alistair"}]


  :break
  [:node-create [:mishmash :facts] :map]


  :break
  [:node-create [:mishmash :facts "a"] :map]
  [:value [:mishmash :facts "a"] nil {:text "b", :keywords "c"}]
  :break
  [:node-create [:mishmash :facts "d"] :map]
  [:value [:mishmash :facts "d"] nil {:text "e", :keywords "f"}]
 ]}