(ns api.core-test
  (:require [api.core :as api]
            [api.exception :as ex]
            [cheshire.core :refer [generate-string
                                   parse-string]]
            [midje.sweet :refer [against-background
                                 fact
                                 facts
                                 =>]]
            [ring.mock.request :as mock])
  (:import (java.time LocalDate)))

(defn to-date [sdate]
  (LocalDate/parse sdate))

(defn parse-body [body]
  (parse-string (slurp body) true))

(defn mock-get-request [url]
  (api/app (mock/request :get url)))

(defn mock-post-request [url body]
  (api/app (-> (mock/request :post url)
               (mock/content-type "application/json")
               (mock/body (generate-string body)))))

(facts "about '/api/server/status'"
       (fact "GET should return 200 and and have a status message"
             (let [response (mock-get-request "/api/server/status")
                   body     (parse-body (:body response))]
               (:status response) => 200
               body => {:data "Server is UP"})))

(facts "about 'not-found-routes'"
       (fact "GET on a non-existant route should return 404 and have a meaningful message"
             (let [response (mock-get-request "/api/does-non-exist")
                   body     (parse-body (:body response))]
               (:status response) => 404
               body => {:data "Route Not Found"})))

(facts "about '/api/records/email'"
       (against-background [(api/get-records) => [{:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :birth-date (to-date "1970-05-29")}
                                                  {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :birth-date (to-date "1977-10-18")}
                                                  {:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :birth-date (to-date "1977-02-16")}
                                                  {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :birth-date (to-date "1982-08-05")}
                                                  {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :birth-date (to-date "1995-03-17")}
                                                  {:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :birth-date (to-date "1918-07-04")}]]
         (fact "GET should return 200 and return a list of records sorted by email"
               (let [response (mock-get-request "/api/records/email")
                     body     (parse-body (:body response))]
                 (:status response) => 200
                 body => {:data [{:lastName "Barton", :firstName "Clint", :email "shared@avengers.com", :favoriteColor "black", :birthDate "1977-02-16"}
                                 {:lastName "Maximoff", :firstName "Wanda", :email "shared@avengers.com", :favoriteColor "red", :birthDate "1995-03-17"}
                                 {:lastName "Romanoff", :firstName "Natasha", :email "shared@avengers.com", :favoriteColor "black", :birthDate "1982-08-05"}
                                 {:lastName "Stark", :firstName "Tony", :email "iron.man@avengers.com", :favoriteColor "red", :birthDate "1970-05-29"}
                                 {:lastName "Rogers", :firstName "Steve", :email "captain.america@avengers.com", :favoriteColor "blue", :birthDate "1918-07-04"}
                                 {:lastName "Lang", :firstName "Scott", :email "ant.man@avengers.com", :favoriteColor "red", :birthDate "1977-10-18"}]}))))

(facts "about '/api/records/birthdate'"
       (against-background [(api/get-records) => [{:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :birth-date (to-date "1970-05-29")}
                                                  {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :birth-date (to-date "1977-10-18")}
                                                  {:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :birth-date (to-date "1977-02-16")}
                                                  {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :birth-date (to-date "1982-08-05")}
                                                  {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :birth-date (to-date "1995-03-17")}
                                                  {:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :birth-date (to-date "1918-07-04")}]]
         (fact "GET should return 200 and return a list of records sorted by birthdate"
               (let [response (mock-get-request "/api/records/birthdate")
                     body     (parse-body (:body response))]
                 (:status response) => 200
                 body => {:data [{:lastName "Rogers", :firstName "Steve", :email "captain.america@avengers.com", :favoriteColor "blue", :birthDate "1918-07-04"}
                                 {:lastName "Stark", :firstName "Tony", :email "iron.man@avengers.com", :favoriteColor "red", :birthDate "1970-05-29"}
                                 {:lastName "Barton", :firstName "Clint", :email "shared@avengers.com", :favoriteColor "black", :birthDate "1977-02-16"}
                                 {:lastName "Lang", :firstName "Scott", :email "ant.man@avengers.com", :favoriteColor "red", :birthDate "1977-10-18"}
                                 {:lastName "Romanoff", :firstName "Natasha", :email "shared@avengers.com", :favoriteColor "black", :birthDate "1982-08-05"}
                                 {:lastName "Maximoff", :firstName "Wanda", :email "shared@avengers.com", :favoriteColor "red", :birthDate "1995-03-17"}]}))))

(facts "about '/api/records/name'"
       (against-background [(api/get-records) => [{:last-name "Stark", :first-name "Tony", :email "iron.man@avengers.com", :favorite-color "red", :birth-date (to-date "1970-05-29")}
                                                  {:last-name "Lang", :first-name "Scott", :email "ant.man@avengers.com", :favorite-color "red", :birth-date (to-date "1977-10-18")}
                                                  {:last-name "Barton", :first-name "Clint", :email "shared@avengers.com", :favorite-color "black", :birth-date (to-date "1977-02-16")}
                                                  {:last-name "Romanoff", :first-name "Natasha", :email "shared@avengers.com", :favorite-color "black", :birth-date (to-date "1982-08-05")}
                                                  {:last-name "Maximoff", :first-name "Wanda", :email "shared@avengers.com", :favorite-color "red", :birth-date (to-date "1995-03-17")}
                                                  {:last-name "Rogers", :first-name "Steve", :email "captain.america@avengers.com", :favorite-color "blue", :birth-date (to-date "1918-07-04")}]]
         (fact "GET should return 200 and return a list of records sorted by last name"
               (let [response (mock-get-request "/api/records/name")
                     body     (parse-body (:body response))]
                 (:status response) => 200
                 body => {:data [{:lastName "Stark", :firstName "Tony", :email "iron.man@avengers.com", :favoriteColor "red", :birthDate "1970-05-29"}
                                 {:lastName "Romanoff", :firstName "Natasha", :email "shared@avengers.com", :favoriteColor "black", :birthDate "1982-08-05"}
                                 {:lastName "Rogers", :firstName "Steve", :email "captain.america@avengers.com", :favoriteColor "blue", :birthDate "1918-07-04"}
                                 {:lastName "Maximoff", :firstName "Wanda", :email "shared@avengers.com", :favoriteColor "red", :birthDate "1995-03-17"}
                                 {:lastName "Lang", :firstName "Scott", :email "ant.man@avengers.com", :favoriteColor "red", :birthDate "1977-10-18"}
                                 {:lastName "Barton", :firstName "Clint", :email "shared@avengers.com", :favoriteColor "black", :birthDate "1977-02-16"}]}))))

(facts "about '/api/records'"
       (against-background [(api/add-record {:data "should-pass"}) => "pass"
                            (api/add-record {:data "duplicate"}) => (ex/throw-ex! {:type :conflict
                                                                                   :title "Record Already Exists"
                                                                                   :message "A record for this person already exists"})
                            (api/add-record {:data "parse-error"}) => (ex/throw-ex! {:type :bad-request
                                                                                     :title "Unable to Parse"
                                                                                     :message "Unable to parse data line"})]
         (fact "POST on /api/records should return 201 and have the location in the header"
               (let [response (mock-post-request "/api/records" {:data "should-pass"})
                     location (-> (:headers response)
                                  (get "Location"))]
                 (:status response) => 201
                 location => "/api/records/pass"))
         (fact "POST on /api/records with a duplicate record should return a 409 with an error message"
               (let [response (mock-post-request "/api/records" {:data "duplicate"})
                     body     (parse-body (:body response))]
                 (:status response) => 409
                 body => {:errors [{:data {:message "A record for this person already exists"
                                           :title "Record Already Exists"
                                           :type "conflict"}
                                    :detail "A record for this person already exists"
                                    :status 409
                                    :title "Record Already Exists"
                                    :type "conflict"}]}))
         (fact "POST on /api/records with an invalid data line should return a 400 with an error message"
               (let [response (mock-post-request "/api/records" {:data "parse-error"})
                     body     (parse-body (:body response))]
                 (:status response) => 400
                 body => {:errors [{:data {:message "Unable to parse data line"
                                           :title "Unable to Parse"
                                           :type "bad-request"}
                                    :detail "Unable to parse data line"
                                    :status 400
                                    :title "Unable to Parse"
                                    :type "bad-request"}]}))))