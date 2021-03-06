(ns api.schemas
  (:require [schema.core :as schema]))

(schema/defschema Record
  {:first-name     schema/Str
   :last-name      schema/Str
   :email          schema/Str
   :favorite-color schema/Str
   :birth-date     schema/Str})

(schema/defschema ParseRecord
  {:data schema/Str})