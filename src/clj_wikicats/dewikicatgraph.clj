(ns clj-wikicats.dewikicatgraph
  (:import (java.io FileReader))
  (require [clojure.data.json :as json]
           [loom.graph :refer :all]
           [loom.alg :refer :all]))

(def category-graph (atom (graph)))

(defn build-german-cat-graph [path-to-json]
  (let [raw-json (slurp (FileReader. path-to-json))
        k1upper-cats (json/read-str raw-json)]
    (doseq [[from k1-uppers] k1upper-cats]
      (loop [upper-cat k1-uppers
             graph @category-graph]
        (if (empty? upper-cat)
          (reset! category-graph graph)
          (recur
            (rest upper-cat)
            (add-edges graph [from (first upper-cat)])))))))

(defn get-a*
  ([from] (get-a* from "!Hauptkategorie"))
  ([from to](astar-path @category-graph from  to (fn [x y] 0))))

(defn get-shortes
  ([from] (get-shortes from "!Hauptkategorie"))
  ([from to](shortest-path @category-graph from  to)))