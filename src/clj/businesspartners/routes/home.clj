(ns businesspartners.routes.home
  (:require
   [businesspartners.layout :as layout]
   [clojure.java.io :as io]
   [businesspartners.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [businesspartners.db.dbbroker :as db]
   [businesspartners.validation :refer [validate-business-partner]]))

(defn save-business-partner [{:keys [params]}]
  (if-let [errors (validate-business-partner params)]
    (response/bad-request {:errors errors})
  (try
    (db/save-partner params)
    (response/ok {:status :ok})
    (catch Exception e
      (response/internal-server-error
        {:errors {:server-error ["Failed to save message!"]}})))))


(defn test-page [request]
  (layout/render
    request
    "test.html"))


(defn home-page [request]
  (layout/render
    request
    "home.html"))

(defn about-page [request]
  (layout/render request "about.html"))

(defn business-partners [request]
  (layout/render request "businesspartners.html"))

(defn businesspartners-list [_]
  (response/ok {:business-partners (vec (db/get-all-partners))}))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/businesspartners" {:get business-partners}]
   ["/getbusinesspartners" {:get businesspartners-list}]
   ["/save-business-partner" {:post save-business-partner}]
   ["/test" {:get test-page}]
   ["/" {:get home-page}]
   ["/about" {:get about-page}]])

