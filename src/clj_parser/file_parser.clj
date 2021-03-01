(ns clj-parser.file-parser
  (:require [clojure.string :as s]
            [util.core :as util]))

(defn read-file
  "Reads in a file and splits it by line so that we have a vector
   containing each line that we can then parse."
  [file]
  (s/split (slurp file) #"\n"))

(defn print-records [message sort-fn records]
  (println message)
  (doseq [{:keys [date-of-birth] :as record} (sort-fn records)]
    (println (format "  %s"
                     (assoc record
                            :date-of-birth (util/date->string date-of-birth))))))

(defn -main [file]
  (let [records (map util/parse-record (read-file file))]
    (print-records "Sorting by Last Name (descending):"
                   util/sort-last-name
                   records)
    (print-records "\nSorting by Email (descending) then Last Name (ascending):"
                   util/sort-email-last-name
                   records)
    (print-records "\nSorting by Birth Date (ascending):"
                   util/sort-birth-date
                   records)))
