(ns util.core
  (:require [clojure.string :as s]))

(def formats-re #"(?: \| |\||, |,| )")

(defn parse-record
  "Takes a record that is either pipe, comma, or space delimited
   and parses it then returns a hashmap of the values."
  [record]
  (let [[lname fname email favorite-color date-of-birth] (s/split record formats-re)]
    {:last-name      lname
     :first-name     fname
     :email          email
     :favorite-color favorite-color
     :date-of-birth  date-of-birth}))

(defn sort-last-name [records]
  (sort-by :last-name #(compare %2 %1) records))
