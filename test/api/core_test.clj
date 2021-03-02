(ns api.core-test
  (:require [api.core :as api]
            [cheshire.core :refer [parse-string]]
            [midje.sweet :refer [fact
                                 facts
                                 =>]]
            [ring.mock.request :as mock]))

(defn parse-body [body]
  (parse-string (slurp body) true))

(defn mock-get-request [url]
  (api/app (mock/request :get url)))

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