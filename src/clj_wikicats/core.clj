(ns clj-wikicats.core
  (:import (java.io FileReader))
  (require [clojure.data.json :as json]
            [loom.graph :refer :all]))

(def category-graph (atom (digraph)))

(defn build-german-cat-graph [path-to-json]
  (let [raw-json (slurp (FileReader. path-to-json))
        k1upper-cats (json/read-str raw-json :key-fn keyword)]
    (doseq [[from k1-uppers] k1upper-cats]
      (loop [upper-cat k1-uppers
             graph @category-graph]
        (if (empty? upper-cat)
          (reset! category-graph graph)
          (recur
            (rest upper-cat)
            (add-edges graph [from (first upper-cat)])))))))

(defn -main [& args]
  (println "Start")
  (build-german-cat-graph "/Users/hagentonnies/workspace/clj-wikicats/resources/de_cat_graph.json")

  (println "Node Count" (count (nodes @category-graph)))
  (print "Nodes")
  (doseq [n (nodes @category-graph)]
    (println n))
  (print "Edges")
  (doseq [n (edges @category-graph)]
    (println n))
  (println "end!"))
