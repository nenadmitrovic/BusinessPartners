(ns businesspartners.validation
  (:require [struct.core :as st]))


(def message-schema
  [[:name
    st/required
    st/string
    {:message "Name can containt not more than 30 characters!"
     :validate (fn [name] (<= (count name) 30))}]
   [:address
    st/required
    st/string
    {:message "Address can containt not more than 30 characters!"
     :validate (fn [name] (<= (count name) 30))}]
   [:phone
    st/required
    st/string
    {:message "Phone can containt not more than 30 characters!"
     :validate (fn [name] (<= (count name) 30))}]
   [:email
    st/required
    st/string
    st/email
    {:message "Email can containt not more than 30 characters!"
     :validate (fn [name] (<= (count name) 30))}]])

(defn validate-business-partner [params]
  (first (st/validate params message-schema)))