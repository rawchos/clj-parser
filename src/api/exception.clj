(ns api.exception
  (:require [clojure.stacktrace :refer [print-stack-trace]]
            [compojure.api.exception :as ex]))

(defn ex-handler [type status title]
  (fn [^Exception ex & _]
    (let [data (or (ex-data ex) {})
          error-message (.getMessage ex)]
      {:status status
       :headers {}
       :body {:errors [{:status status
                        :type   type
                        :title  (or (:title data) title)
                        :detail error-message
                        :data   data}]}})))

(def handlers
  {:handlers {:not-found (ex-handler :not-found 404 "Not Found")
              :bad-request (ex-handler :bad-request 400 "Bad Request")
              :conflict (ex-handler :conflict 409 "Conflict")
              :internal-server-error (ex-handler :internal-server-error 500 "Internal Server Error")
              ::ex/default (ex-handler :unknown 500 "Internal Server Error")}})

(defn stacktrace-str [exception]
  (with-out-str (print-stack-trace exception)))

(defn throw-ex! [{:keys [exception message] :as detail-map}]
  (when exception (println (stacktrace-str exception)))
  (throw (ex-info (or message "Unspecified Error - See Detail") detail-map)))