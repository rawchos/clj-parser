(ns api.core
  (:require [api.schemas :refer [Record]]
            [compojure.api.sweet :refer [api
                                         context
                                         GET
                                         POST
                                         undocumented]]
            [compojure.route :refer [not-found]]
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

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Clojure Parser"
                   :description "Create and View records parsed from comma, space, and pipe delimited formats."}}}}
   
   (context "/api" []
     :tags ["api"]
     
     (GET "/records/email" []
       :return {:data [Record]}
       :summary "Returns a list of Records sorted by email"
       (ok {:data (util/sort-email-last-name (get-records))}))

     (GET "/server/status" []
       :return {:data schema/Str}
       :description "Returns server status for basic health monitoring"
       (ok {:data "Server is UP"}))
     
     (undocumented
      (not-found (ok {:data "Route Not Found"}))))))

(defn start [port]
  (jetty/run-jetty app {:port port}))