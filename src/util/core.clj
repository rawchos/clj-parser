(ns util.core
  (:require [clojure.string :as s])
  (:import (java.time LocalDate)))

(def formats-re #"(?: \| |\||, |,| )")

(defn parse-record
  "Takes a record that is either pipe, comma, or space delimited
   and parses it then returns a hashmap of the values. Assumes birth
   dates are in the form of: 2021-03-01. Could maybe be switched to
   allow multiple formats by defining a set of formatters and looping
   through them but then we run the risk of days/months being interposed."
  [record]
  (let [[lname fname email favorite-color date-of-birth] (s/split record formats-re)]
    {:last-name      lname
     :first-name     fname
     :email          email
     :favorite-color favorite-color
     :date-of-birth  (LocalDate/parse date-of-birth)}))

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
  (sort-by :date-of-birth records))
