(ns api.core
  (:require [api.exception :refer [handlers
                                   throw-ex!]]
            [api.schemas :refer [ParseRecord
                                 Record]]
            [camel-snake-kebab.core :refer [->camelCase
                                            ->kebab-case]]
            [clojure.string :refer [lower-case]]
            [compojure.api.sweet :refer [api
                                         context
                                         GET
                                         POST
                                         undocumented]]
            [compojure.route :refer [not-found]]
            [muuntaja.core :as muuntaja]
            [ring.adapter.jetty :as jetty]
            [ring.util.http-response :refer [created
                                             ok]]
            [schema.core :as schema]
            [util.core :as util]))

(def records (atom (util/default-db)))

(defn get-records
  "This is a wrapper for the records atom just so we can override it
   in our tests."
  []
  @records)

(defn add-record [{data-line :data}]
  (let [record (try
                 (util/parse-record data-line)
                 (catch Exception _ (throw-ex! {:type :bad-request
                                                :title "Unable to Parse"
                                                :message "Unable to parse data line"})))]
    (if ((set (get-records)) record)
      (throw-ex! {:type :conflict
                  :title "Record Already Exists"
                  :message "A record for this person already exists"
                  :error-data record})
      (swap! records conj record))
    (lower-case (:last-name record))))

(defn encode-key [k]
  (->camelCase (name k)))

(defn decode-key [k]
  (keyword (->kebab-case k)))

;; Create a muuntaja instance to handle how we convert keywords
;; to and from json
(def muuntaja-instance
  (muuntaja/create
   (-> muuntaja/default-options
       (update-in [:formats "application/json"]
                  merge
                  {:encoder-opts {:encode-key-fn encode-key}
                   :decoder-opts {:decode-key-fn decode-key}}))))

(def app
  (api
   {:formats muuntaja-instance
    :swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Clojure Parser"
                   :description "Create and View records parsed from comma, space, and pipe delimited formats."}}}
    :exceptions handlers}
   
   (context "/api" []
     :tags ["api"]

     (POST "/records" []
       :body [record ParseRecord]
       :summary "Creates a new Record if it doesn't already exist."
       (created (str "/api/records/" (add-record record))))
     
     (GET "/records/email" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by email descending and last name ascending"
       (ok {:data (util/sort-email-last-name (get-records))}))

     (GET "/records/birthdate" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by birthdate ascending"
       (ok {:data (util/sort-birth-date (get-records))}))
     
     (GET "/records/name" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by lastname descending"
       (ok {:data (util/sort-last-name (get-records))}))

     (GET "/server/status" []
       :return {:data schema/Str}
       :description "Returns server status for basic health monitoring"
       (ok {:data "Server is UP"}))
     
     (undocumented
      (not-found (ok {:data "Route Not Found"}))))))

(defn start [port]
  (jetty/run-jetty app {:port port}))