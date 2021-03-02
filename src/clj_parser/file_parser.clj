(ns clj-parser.file-parser
  (:require [clojure.string :as s]
            [util.core :as util]))

(defn read-file
  "Reads in a file and splits it by line so that we have a vector
   containing each line that we can then parse."
  [file]
  (s/split (slurp file) #"\n"))

(defn print-records [message sort-fn records]
  (println (format "\n%s" message))
  (doseq [{:keys [birth-date] :as record} (sort-fn records)]
    (println
     (format "  %s"
             (assoc record :birth-date (util/date->string birth-date))))))

(def print-config
  [{:message "Sorting by Last Name (descending):" :sort-fn util/sort-last-name}
   {:message "Sorting by Email (descending) then Last Name (ascending):" :sort-fn util/sort-email-last-name}
   {:message "Sorting by Birth Date (ascending):" :sort-fn util/sort-birth-date}])

(defn -main [file]
  (let [records (map util/parse-record (read-file file))]
    (doseq [{:keys [message sort-fn]} print-config]
      (print-records message sort-fn records))))
