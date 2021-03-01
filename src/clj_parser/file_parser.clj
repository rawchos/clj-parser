(ns clj-parser.file-parser
  (:require [clojure.string :as s]
            [util.core :as util]))

(defn read-file
  "Reads in a file and splits it by line so that we have a vector
   containing each line that we can then parse."
  [file]
  (s/split (slurp file) #"\n"))

(defn -main [file]
  (let [records (map util/parse-record (read-file file))]
    (println records)))

; (read-file "resources/samples/pipe-delimited1.txt")
