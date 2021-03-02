(ns api.core
  (:require [compojure.api.sweet :refer [api
                                         context
                                         GET
                                         POST
                                         undocumented]]
            [compojure.route :refer [not-found]]
            [ring.adapter.jetty :as jetty]
            [ring.util.http-response :refer [created
                                             ok]]
            [schema.core :as schema]))

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Clojure Parser"
                   :description "Create and View records parsed from comma, space, and pipe delimited formats."}}}}
   
   (context "/api" []
     :tags ["api"]

     (GET "/server/status" []
       :return {:data schema/Str}
       :description "Returns server status for basic health monitoring"
       (ok {:data "Server is UP"}))
     
     (undocumented
      (not-found (ok {:data "Route Not Found"}))))))

(defn start [port]
  (jetty/run-jetty app {:port port}))