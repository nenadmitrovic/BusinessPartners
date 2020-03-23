(ns businesspartners.routes.services
  (:require [businesspartners.businesspartners :as bp]
            [businesspartners.middleware :as middleware]
            [ring.util.http-response :as response]))

(defn service-routes []
  ["/api"
   {:middleware [middleware/wrap-formats]}
   ["/business-partners"
    {:get
     (fn [_]
       (response/ok (bp/business-partners-list)))}]
   ["/business-partner"
    {:post
     (fn [{:keys [params]}]
       (try
         (bp/save-business-partner params)
         (response/ok {:status :ok})
         (catch Exception e
           (let [{id :business-partners/error-id
                  errors :errors} (ex-data e)]
             (case id
               :validation
               (response/bad-request {:errors errors})
               (response/internal-server-error
                 {:errors {:server-error ["Failed to save message!"]}}))))))}]])
