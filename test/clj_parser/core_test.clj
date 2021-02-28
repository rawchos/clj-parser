(ns clj-parser.core-test
  (:require [clj-parser.core :as core]
            [midje.sweet :refer [fact
                                 facts
                                 =>]]))
(facts "about 'contrived-test'"
       (fact "the contrived test should return true"
             (core/contrived-test) => true))
