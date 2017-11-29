(ns oops.tools
  (:require-macros [oops.tools])
  (:require [cljs.test :refer-macros [deftest testing is are run-tests use-fixtures]]
            [oops.config :as config]
            [oops.helpers :refer [unchecked-aget unchecked-aset]]
            [devtools.core]))

; -- console recording ------------------------------------------------------------------------------------------------------

(defonce console-recorders (atom []))

(defn add-console-recorder! [recorder]
  (swap! console-recorders conj recorder))

(defn remove-console-recorder! [recorder]
  (swap! console-recorders (fn [recorders] (remove #{recorder} recorders))))

; -- capturing console output -----------------------------------------------------------------------------------------------

(defn console-handler [orig-fn kind & args]
  (let [msg (str kind args)
        recorders @console-recorders]
    (if-not (empty? recorders)
      (doseq [recorder recorders]
        (swap! recorder conj msg))
      (.apply orig-fn js/console (to-array args)))))

(defn store-console-api []
  {"log"   (unchecked-aget js/window "console" "log")
   "warn"  (unchecked-aget js/window "console" "warn")
   "info"  (unchecked-aget js/window "console" "info")
   "error" (unchecked-aget js/window "console" "error")})

(defn captured-console-api [original-api]
  {"log"   (partial console-handler (get original-api "log") "LOG: ")
   "warn"  (partial console-handler (get original-api "warn") "WARN: ")
   "info"  (partial console-handler (get original-api "info") "INFO: ")
   "error" (partial console-handler (get original-api "error") "ERROR: ")})

(defn set-console-api! [api]
  (unchecked-aset js/window "console" "log" (get api "log"))
  (unchecked-aset js/window "console" "warn" (get api "warn"))
  (unchecked-aset js/window "console" "info" (get api "info"))
  (unchecked-aset js/window "console" "error" (get api "error")))

(defonce original-console-api (atom nil))

(defn start-console-capture! []
  {:pre [(nil? @original-console-api)]}
  (reset! original-console-api (store-console-api))
  (set-console-api! (captured-console-api @original-console-api)))

(defn stop-console-capture! []
  {:pre [(some? @original-console-api)]}
  (set-console-api! @original-console-api)
  (reset! original-console-api nil))

(defn with-captured-console [f]
  (start-console-capture!)
  (f)
  (stop-console-capture!))

; -- testing config assumptions ---------------------------------------------------------------------------------------------

(defn presume-runtime-config [config]
  (let [runtime-config (select-keys (config/get-current-runtime-config) (keys config))]
    (is (= runtime-config config))))
