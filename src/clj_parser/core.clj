(ns clj-parser.core
  (:require [api.core :as api])
  (:gen-class))

(defn contrived-test [] true)

;; TODO: Maybe allow port to be passed in and default to 8080 otherwise?
(defn -main []
  (api/start 8080))
