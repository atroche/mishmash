(ns tutorial-client.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro tutorial-client-templates
  []
  {:datum-list (dtfn (tnodes "tutorial-client.html" "datum-list" [[:ul.data]]) #{:id})
   :datum (tfn (tnodes "tutorial-client.html" "datum"))})