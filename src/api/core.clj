(ns api.core
  (:require [api.exception :refer [handlers
                                   throw-ex!]]
            [api.schemas :refer [ParseRecord
                                 Record]]
            [camel-snake-kebab.core :refer [->camelCase
                                            ->kebab-case]]
            [clojure.string :refer [lower-case]]
            [clojure.walk :refer [postwalk]]
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

(defn old-add-record [{data-line :data}]
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


(defn add-record [record]
  (if ((set (get-records)) record)
    (throw-ex! {:type :conflict
                :title "Record Already Exists"
                :message "A record for this person already exists"
                :error-data record})
    (swap! records conj record))
  (lower-case (:last-name record)))

(defn update-record [{:keys [birth-date] :as record} convert-fn]
  (assoc record :birth-date (convert-fn birth-date)))

(defn convert-date [records convert-fn]
   (postwalk (fn [x]
               (if (map? x)
                 (update-record x convert-fn)
                 x))
             records))

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
       :body [record Record]
       :summary "Creates a new Record if it doesn't already exist."
       (created (str "/api/records/" (add-record (convert-date record
                                                               util/string->date)))))
     
     (GET "/records/email" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by email descending and last name ascending"
       (ok {:data (-> (get-records)
                      (util/sort-email-last-name)
                      (convert-date util/date->string))}))

     (GET "/records/birthdate" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by birthdate ascending"
       (ok {:data (-> (get-records)
                      (util/sort-birth-date)
                      (convert-date util/date->string))}))
     
     (GET "/records/name" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by lastname descending"
       (ok {:data (-> (get-records)
                      (util/sort-last-name)
                      (convert-date util/date->string))}))

     (GET "/server/status" []
       :return {:data schema/Str}
       :description "Returns server status for basic health monitoring"
       (ok {:data "Server is UP"}))
     
     (undocumented
      (not-found (ok {:data "Route Not Found"}))))))

(defn start [port]
  (jetty/run-jetty app {:port port}))