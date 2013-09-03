(ns mishmash.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro mishmash-templates
  []
  {:app (dtfn (tnodes "app.html" "app" []) #{:id})
   :fact-list (dtfn (tnodes "mishmash.html" "fact-list" [[:tbody]]) #{:id})
   :fact (dtfn (tnodes "mishmash.html" "fact") #{:id})
   :auth-status (dtfn (tnodes "auth-status.html" "auth-status") #{:id})})