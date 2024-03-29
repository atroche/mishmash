(defproject mishmash "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :repositories {
    "sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"
  }
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1847"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [domina "1.0.1"]
                 [ch.qos.logback/logback-classic "1.0.7" :exclusions [org.slf4j/slf4j-api]]
                 [io.pedestal/pedestal.app "0.2.1"]
                 [io.pedestal/pedestal.app-tools "0.2.1"]]
  :profiles {:dev {:source-paths ["dev"]}}
  :min-lein-version "2.0.0"
  :source-paths ["app/src" "app/templates"]
  :resource-paths ["config"]
  :target-path "out/"
  :aliases {"dumbrepl" ["trampoline" "run" "-m" "clojure.main/main"]})
