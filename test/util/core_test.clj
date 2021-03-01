(ns util.core-test
  (:require [midje.sweet :refer [fact
                                 facts
                                 =>]]
            [util.core :as u])
  (:import (java.time LocalDate)))

(defn to-date [s]
  (LocalDate/parse s))

(facts "about 'parse-record'"
       (fact "should handle pipe delimited records"
             (u/parse-record "Stark|Tony|iron.man@avengers.com|red|1970-05-29") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth (to-date "1970-05-29")}
             (u/parse-record "Stark | Tony | iron.man@avengers.com | red | 1970-05-29") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth (to-date "1970-05-29")})
       (fact "should handle comma delimited records"
             (u/parse-record "Stark,Tony,iron.man@avengers.com,red,1970-05-29") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth (to-date "1970-05-29")}
             (u/parse-record "Stark, Tony, iron.man@avengers.com, red, 1970-05-29") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth (to-date "1970-05-29")})
       (fact "should handle space delimited records"
             (u/parse-record "Stark Tony iron.man@avengers.com red 1970-05-29") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth (to-date "1970-05-29")}))

(def test-records
  [{:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :date-of-birth (to-date "1970-05-29")}
   {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :date-of-birth (to-date "1977-10-18")}
   {:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :date-of-birth (to-date "1977-02-16")}
   {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :date-of-birth (to-date "1982-08-05")}
   {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :date-of-birth (to-date "1995-03-17")}
   {:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :date-of-birth (to-date "1918-07-04")}])

(facts "about 'sort-last-name'"
       (fact "should sort a list of records descending by last name"
             (u/sort-last-name test-records) => [{:date-of-birth (to-date "1970-05-29") :email "iron.man@avengers.com" :favorite-color "red" :first-name "Tony" :last-name "Stark"}
                                                 {:date-of-birth (to-date "1982-08-05") :email "shared@avengers.com" :favorite-color "black" :first-name "Natasha" :last-name "Romanoff"}
                                                 {:date-of-birth (to-date "1918-07-04") :email "captain.america@avengers.com" :favorite-color "blue" :first-name "Steve" :last-name "Rogers"}
                                                 {:date-of-birth (to-date "1995-03-17") :email "shared@avengers.com" :favorite-color "red" :first-name "Wanda" :last-name "Maximoff"}
                                                 {:date-of-birth (to-date "1977-10-18") :email "ant.man@avengers.com" :favorite-color "red" :first-name "Scott" :last-name "Lang"}
                                                 {:date-of-birth (to-date "1977-02-16") :email "shared@avengers.com" :favorite-color "black" :first-name "Clint" :last-name "Barton"}]))

(facts "about 'sort-email-last-name'"
       (fact "should sort by email descending then last name ascending"
             (u/sort-email-last-name test-records) => [{:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :date-of-birth (to-date "1977-02-16")}
                                                       {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :date-of-birth (to-date "1995-03-17")}
                                                       {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :date-of-birth (to-date "1982-08-05")}
                                                       {:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :date-of-birth (to-date "1970-05-29")}
                                                       {:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :date-of-birth (to-date "1918-07-04")}
                                                       {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :date-of-birth (to-date "1977-10-18")}]))

(facts "about 'sort-birth-date'"
       (fact "should sort by birth date ascending"
             (u/sort-birth-date test-records) => [{:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :date-of-birth (to-date "1918-07-04")}
                                                  {:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :date-of-birth (to-date "1970-05-29")}
                                                  {:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :date-of-birth (to-date "1977-02-16")}
                                                  {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :date-of-birth (to-date "1977-10-18")}
                                                  {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :date-of-birth (to-date "1982-08-05")}
                                                  {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :date-of-birth (to-date "1995-03-17")}]))