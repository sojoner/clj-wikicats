(defproject clj-wikicats "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [org.clojure/data.json "0.2.5"]
                 [aysylu/loom "0.5.0"]
                 [http-kit "2.1.19"]
                 [ring "1.3.1"]
                 [ring/ring-defaults "0.1.1"]
                 [compojure "1.1.9"]
                 [com.stuartsierra/component "0.2.2"]
                 [org.clojure/tools.logging "0.3.0"]
                 [ch.qos.logback/logback-classic "1.1.1"]]
  :source-paths ["src/"]
  :main ^:skip-aot clj-wikicats.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
