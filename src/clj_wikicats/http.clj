(ns clj-wikicats.http
  (:gen-class)
  (:require
    [clj-wikicats.dewikicatgraph :as dewikigraph]
    [clojure.tools.logging :as log]
    [org.httpkit.server :as http-kit-server]
    [ring.middleware.defaults]
    [ring.util.response :refer [resource-response response content-type]]
    [compojure.core :as comp :refer (defroutes GET POST)]
    [compojure.route :as route]
    [com.stuartsierra.component :as component]
    [clojure.data.json :as json]))

(def ring-defaults-config (assoc-in ring.middleware.defaults/site-defaults [:security :anti-forgery]
                                    {:read-token (fn [req] (-> req :params :csrf-token))}))
(defn- static-html [file-name] (content-type (resource-response file-name {:root "public"}) "text/html"))

(defn- json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str data)})

(defrecord Httpserver [conf server]
  component/Lifecycle
  (start [component] (log/info "Starting HTTP Component")
    (defroutes my-routes                                    ; created during start so that the correct communicator instance is used
               (GET "/" [] (static-html "index.html"))
               (GET "/shortest-path" [& parameter] (json-response (dewikigraph/get-shortes
                                                                    (get-in parameter [:from] "Berlin")
                                                                    (get-in parameter [:to] "!Hauptkategorie"))))
               (GET "/astar-path" [& parameter] (json-response (dewikigraph/get-a*
                                                                 (get-in parameter [:from] "Berlin")
                                                                 (get-in parameter [:to] "!Hauptkategorie"))))
               (route/resources "/")                        ; Static files, notably public/main.js (our cljs target)
               (route/not-found "Page not found"))
    (let [my-ring-handler (ring.middleware.defaults/wrap-defaults my-routes ring-defaults-config)
          server (http-kit-server/run-server my-ring-handler {:port (:port conf)})
          uri (format "http://localhost:%s/" (:local-port (meta server)))]
      (log/info "Http-kit server is running at" uri)
      (assoc component :server server)))
  (stop [component] (log/info "Stopping HTTP Server")
    (server :timeout 100)
    (assoc component :server nil)))

(defn new-http-server [conf]
  (dewikigraph/build-german-cat-graph "resources/data/de_cat_graph.json")
  (map->Httpserver {:conf conf}))
