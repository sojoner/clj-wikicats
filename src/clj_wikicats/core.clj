(ns clj-wikicats.core
  (:import (java.io FileReader))
  (require [clojure.data.json :as json]
            [loom.graph :refer :all]
            [loom.alg :refer :all]))

(def category-graph (atom (digraph)))

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

(defn -main [& args]
  (println "Start")
  (build-german-cat-graph "/Users/hagen/workspace/clj-wikicats/resources/de_cat_graph.json")

  (println "Node Count" (count (nodes @category-graph)))
  (println "Elefant_in_der_Kunst -> !Hauptkategorie" (time (astar-path @category-graph "Elefant_in_der_Kunst"  "!Hauptkategorie" (fn [x y] 0))) )
  (println "Elefant_in_der_Kunst -> !Hauptkategorie" (time (shortest-path @category-graph "Elefant_in_der_Kunst"  "!Hauptkategorie") ) )
  (println "Angela_Merkel ->  Sachsystematik" (time (astar-path @category-graph "Angela_Merkel"  "Sachsystematik" (fn [x y] 0))))
  (println "Polymerbildende_Reaktion -> Sachsystematik" (time (shortest-path @category-graph "Polymerbildende_Reaktion"  "Sachsystematik") ) )
  (println "Data-Mining -> !Hauptkategorie"(time (shortest-path @category-graph "Data-Mining"  "!Hauptkategorie")) )
  (println "end!"))
