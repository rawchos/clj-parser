(ns util.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as s])
  (:import (java.io PushbackReader)
           (java.time LocalDate)
           (java.time.format DateTimeFormatter)))

(defn spit-edn
  "Convenience function for writing out edn to the filesystem"
  [filename data]
  (spit filename (with-out-str (prn data))))

(defn read-edn
  "Convenience function for reading in edn data from the filesystem"
  [filename]
  (edn/read (PushbackReader. (io/reader filename))))

(defn date->string [date]
  (.format date (DateTimeFormatter/ofPattern "M/d/yyyy")))

(defn string->date [sdate]
  (LocalDate/parse sdate))

(defn convert-date [record]
  (reduce-kv
   (fn [m k v]
     (if (= k :birth-date)
       (assoc m k (string->date v))
       (assoc m k v)))
   {}
   record))

(defn default-db []
  (->> (read-edn "resources/api/default-records.edn")
       (map convert-date)))

(def formats-re #"(?: \| |\||, |,| )")

(defn parse-record
  "Takes a record that is either pipe, comma, or space delimited
   and parses it then returns a hashmap of the values. Assumes birth
   dates are in the form of: 2021-03-01. Could maybe be switched to
   allow multiple formats by defining a set of formatters and looping
   through them but then we run the risk of days/months being interposed."
  [record]
  (let [[lname fname email favorite-color birth-date] (s/split record formats-re)]
    {:last-name      lname
     :first-name     fname
     :email          email
     :favorite-color favorite-color
     :birth-date     (string->date birth-date)}))

(def asc compare)
(def desc #(compare %2 %1))

(defn sort-last-name [records]
  (sort-by :last-name desc records))

(defn compare-by [[k comp & more] first-value second-value]
  (let [result (comp (k first-value) (k second-value))]
    (if (and (zero? result)
             (seq more))
      (recur more first-value second-value)
      result)))

(defn sort-email-last-name
  "This function is a little tricky because we're sorting by email
   descending then by last name ascending. This makes it to where we
   can't just (juxt :email :last-name) because each needs its own
   sorting direction."
  [records]
  (sort #(compare-by [:email desc :last-name asc] %1 %2) records))

(defn sort-birth-date [records]
  (sort-by :birth-date records))
