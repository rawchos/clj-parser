(ns util.core-test
  (:require [midje.sweet :refer [fact
                                 facts
                                 =>]]
            [util.core :as u]))

(facts "about 'parse-record'"
       (fact "should handle pipe delimited records"
             (u/parse-record "Stark|Tony|iron.man@avengers.com|red|5/29/1970") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth "5/29/1970"}
             (u/parse-record "Stark | Tony | iron.man@avengers.com | red | 5/29/1970") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth "5/29/1970"})
       (fact "should handle comma delimited records"
             (u/parse-record "Stark,Tony,iron.man@avengers.com,red,5/29/1970") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth "5/29/1970"}
             (u/parse-record "Stark, Tony, iron.man@avengers.com, red, 5/29/1970") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth "5/29/1970"})
       (fact "should handle space delimited records"
             (u/parse-record "Stark Tony iron.man@avengers.com red 5/29/1970") => {:first-name "Tony" :last-name "Stark" :email "iron.man@avengers.com" :favorite-color "red" :date-of-birth "5/29/1970"}))

(def test-records
  [{:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :date-of-birth "5/29/1970"}
   {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :date-of-birth "10/18/1977"}
   {:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :date-of-birth "2/16/1977"}
   {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :date-of-birth "8/5/1982"}
   {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :date-of-birth "3/17/1995"}
   {:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :date-of-birth "7/4/1918"}])

(facts "about 'sort-last-name'"
       (fact "should sort a list of records descending by last name"
             (u/sort-last-name test-records) => '({:date-of-birth "5/29/1970" :email "iron.man@avengers.com" :favorite-color "red" :first-name "Tony" :last-name "Stark"}
                                                  {:date-of-birth "8/5/1982" :email "shared@avengers.com" :favorite-color "black" :first-name "Natasha" :last-name "Romanoff"}
                                                  {:date-of-birth "7/4/1918" :email "captain.america@avengers.com" :favorite-color "blue" :first-name "Steve" :last-name "Rogers"}
                                                  {:date-of-birth "3/17/1995" :email "shared@avengers.com" :favorite-color "red" :first-name "Wanda" :last-name "Maximoff"}
                                                  {:date-of-birth "10/18/1977" :email "ant.man@avengers.com" :favorite-color "red" :first-name "Scott" :last-name "Lang"}
                                                  {:date-of-birth "2/16/1977" :email "shared@avengers.com" :favorite-color "black" :first-name "Clint" :last-name "Barton"})))