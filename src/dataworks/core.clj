(ns dataworks.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn keyify
  "validates a string and converts to key"
  [str]
  (keyword (clojure.string/replace (clojure.string/trim str) #" " "-")))

(defn header-key-list
  "turns a comma seperated list of strings into a list of keywords"
  [str]
  (into [] (map keyify (clojure.string/split str #","))))

(defn create-entity
  "turns the data body row into map with header-values as keys"
  [keys output input ]
  (conj output (reduce (fn [entity [key val]] 
                         (assoc entity key (clojure.string/trim val))) 
                          {}
                          (map vector keys (clojure.string/split input #",")))))

(defn create-entities
  "operates on the entire imported data body to convert rows into a set of maps"
  [keylist databody]
  (reduce (partial create-entity keylist) [] databody))

(defn parse-csv 
  "parses an imported csv"
  [coll]
  (create-entities (header-key-list (first coll)) (rest coll)))

(defn import-csv 
  "imports a csv file into program"
  [file]
  (parse-csv (clojure.string/split-lines (slurp file))))

(defn my-import
  "imports a format of specified file into program"
  [format file]
  (if (= format "csv")
    import-csv(file)))
