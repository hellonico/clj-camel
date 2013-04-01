(def camel-version "2.10.4")

(defproject clj-camel "1.0.1"
  :description "Clojure library wrapping the Apache Camel Java DSL"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.apache.camel/camel-core ~camel-version]
                 [org.apache.camel/camel-jms ~camel-version]
                 [org.apache.activemq/activemq-camel "5.8.0"]
                 [org.clojure/tools.logging "0.2.3"]])