(ns clj-wikicats.core
  (:gen-class)
  (:require
    [clojure.edn :as edn]
    [clojure.tools.logging :as log]
    [com.stuartsierra.component :as component]
    [clj-wikicats.http :as http]))


(def conf (edn/read-string (slurp "resources/conf.edn")))

(defn get-system [conf]
  "Create system by wiring individual components so that component/start
  will bring up the individual components in the correct order."
  (component/system-map
    :http  (component/using (http/new-http-server conf) {})))

(def system (get-system conf))

(defn -main [& args]
  (log/info "Application started")
  (alter-var-root #'system component/start))
