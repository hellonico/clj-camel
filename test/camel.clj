(require '[clj-camel.core :as c])
(import 'org.apache.camel.LoggingLevel)
(import '[org.apache.camel.impl SimpleRegistry DefaultCamelContext])
; (import 'org.apache.activemq.ActiveMQConnectionFactory)
(import 'org.apache.activemq.camel.component.ActiveMQComponent)

(defn test-bean [exchange body]
  (even? body))

(def error-handler 
  [[:error-handler (c/defaultErrorHandler)]
   [:log-stack-trace true]
   [:log-retry-stack-trace true]
   [:log-handled true]
   [:log-exhausted true]
   [:retry-attempted-log-level LoggingLevel/WARN]
   [:redelivery-delay 1000]
   [:maximum-redeliveries 3]])

(def test-routes
[
  [[:from "file://in"]
   [:to "file://out"]]

  [
  [:from "file://in2"]
  [:to "activemq:queue:FOO.BAR"]]
])

(def activemq-url "tcp://localhost:61616")

(defn start-camel-context []
  (let [r (SimpleRegistry.)
        ctx (doto (DefaultCamelContext. r))]
    (.put r "test-bean" test-bean)
    (.addComponent ctx "activemq" (ActiveMQComponent/activeMQComponent activemq-url))
    (c/add-routes ctx (cons error-handler test-routes))
    (.start ctx)
    ctx))

(def camel-context (start-camel-context))

; send a direct message
(def producer (.createProducerTemplate camel-context))
(.sendBody producer "file://in" "hello")

; once finished
; (.stop camel-context)