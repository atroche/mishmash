(ns mishmash.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro mishmash-templates
  []
  {:fact-list (dtfn (tnodes "mishmash.html" "fact-list" [[:tbody]]) #{:id})
   :fact (dtfn (tnodes "mishmash.html" "fact") #{:id})})