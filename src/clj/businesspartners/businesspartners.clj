(ns businesspartners.businesspartners
  (:require [businesspartners.db.dbbroker :as db]
            [businesspartners.validation :refer [validate-business-partner]]
            [businesspartners.middleware :as middleware]
            [ring.util.http-response :as response]))

(defn business-partners-list []
  {:business-partners (vec (db/get-all-partners))})


(defn save-business-partner [business-partner]
  (if-let [errors (validate-business-partner business-partner)]
    (throw (ex-info "Message is invalid"
                    {:business-partners/error-id :validation
                     :errors errors}))
    (db/save-partner business-partner)))
