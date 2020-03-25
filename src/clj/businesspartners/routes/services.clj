(ns businesspartners.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [businesspartners.businesspartners :as bp]
    [businesspartners.middleware :as middleware]
    [ring.util.http-response :as response]
    [businesspartners.db.dbbroker :as db]))

(defn service-routes []
  ["/api"
   {:middleware [middleware/wrap-formats]
    :swagger {:id ::api}}
   ["" {:no-doc true}
    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]
    ["/swagger-ui*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"})}]]
   ["/business-partners"
    {:get
     (fn [_]
       (response/ok (bp/business-partners-list)))}]
   ["/business-partner"
    {:post
     (fn [{:keys [params]}]
       (try
         (bp/save-business-partner params)
         (response/ok (bp/business-partners-list))
         (catch Exception e
           (let [{id :business-partners/error-id
                  errors :errors} (ex-data e)]
             (case id
               :validation
               (response/bad-request {:errors errors})
               (response/internal-server-error
                 {:errors {:server-error ["Failed to save message!"]}}))))))}]
   ["/delete-business-partner"
    {:post
     (fn [{:keys [params]}]
       (let [id (get params :id)]
         (do
           (bp/delete-partner-by-id id)
           (response/ok (bp/business-partners-list)))))}]
   ["/get-partner-by-id"
    {:get
     (fn [{:keys [params]}]
       (let [id (get params :id)]
         (response/ok {:business-partner (bp/get-partner-by-id id)})))}]
   ["/update-partner-by-id"
    {:post
     (fn [{:keys [params]}]
       (do
         (bp/update-partner-by-id (:id params) (:business-partner params))
         (response/ok)))}]])