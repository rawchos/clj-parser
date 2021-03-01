(ns clj-parser.file-parser
  (:require [clojure.string :as s]
            [util.core :as util]))

(defn read-file
  "Reads in a file and splits it by line so that we have a vector
   containing each line that we can then parse."
  [file]
  (s/split (slurp file) #"\n"))

(defn print-by-last-name
  "This function takes a list of records, sorts them by last name
   descending, then prints them out."
  [records]
  (doseq [record (util/sort-last-name records)]
    (println record)))

(defn print-by-email-last-name [records]
  (doseq [record (util/sort-email-last-name records)]
    (println record)))

(defn -main [file]
  (let [records (map util/parse-record (read-file file))]
    (println "Sorting by Last Name (descending):")
    (print-by-last-name records)
    (println "\nSorting by Email (descending) then Last Name (ascending):")
    (print-by-email-last-name records)))
