(ns businesspartners.validation
  (:require [struct.core :as st]))


(def message-schema
  [[:name
    st/required
    st/string]
   [:address
    st/required
    st/string]
   [:phone
    st/required
    st/string]
   [:email
    st/required
    st/string
    st/email]])

(defn validate-business-partner [params]
  (first (st/validate params message-schema)))