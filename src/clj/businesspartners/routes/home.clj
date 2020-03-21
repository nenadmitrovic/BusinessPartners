(ns businesspartners.routes.home
  (:require
   [businesspartners.layout :as layout]
   [clojure.java.io :as io]
   [businesspartners.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [businesspartners.db.dbbroker :as db]
   [struct.core :as st]))

(def message-schema
  [[:name
    st/required
    st/string]
   [:adress
    st/required
    st/string]
   [:phone
    st/required
    st/string]
   [:email
    st/required
    st/string
    st/email]])

(defn validate-params [params]
  (first (st/validate params message-schema)))


(defn save-business-partner [{:keys [params]}]
  (if-let [errors (validate-params params)]
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

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/save-business-partner" {:post save-business-partner}]
   ["/test" {:get test-page}]
   ["/" {:get home-page}]
   ["/about" {:get about-page}]])

