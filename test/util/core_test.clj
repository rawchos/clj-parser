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