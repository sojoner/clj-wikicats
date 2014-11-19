(ns clj-wikicats.wikipediacategories2json
  (require [clojure.java.jdbc :as j]
           [clojure.data.json :as json]
           ))

(def mysql-db {:subprotocol "mysql"
               :subname     "//192.168.178.24:3306/wikipedia"
               :user        "root"
               :password    ""})

(defn query-from-to-categorys [query]
  (def adjacency-list (atom {}))
  (j/query mysql-db [query] :row-fn (fn [row]
                                      (let [graph @adjacency-list]
                                        (if (contains? graph (nth row 0))
                                          (swap! adjacency-list update-in [(nth row 0)] concat #{(nth row 1)})
                                          (swap! adjacency-list assoc (nth row 0) #{(nth row 1)}))))
           :as-arrays? true))

(defn compute-upper-k1-categories []
  (query-from-to-categorys "
    SELECT CONVERT(page_title USING utf8),CONVERT(cl_to USING utf8)
    FROM `page`
    JOIN `categorylinks` ON `categorylinks`.`cl_from` = `page`.`page_id`
    WHERE `page`.`page_namespace` = 14;")

  (spit "resources/de_cat_graph.json" (json/write-str @adjacency-list)))
