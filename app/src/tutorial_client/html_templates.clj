(ns tutorial-client.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro tutorial-client-templates
  []
  {:fact-list (dtfn (tnodes "tutorial-client.html" "fact-list" [[:tbody]]) #{:id})
   :fact (dtfn (tnodes "tutorial-client.html" "fact") #{:id})})