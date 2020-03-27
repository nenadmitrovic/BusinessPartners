(ns businesspartners.routes.home
  (:require
   [businesspartners.layout :as layout]
   [businesspartners.businesspartners :as bp]
   [businesspartners.middleware :as middleware]
   [ring.util.http-response :as response]))


(defn test-page [request]
  (layout/render
    request
    "test.html"))


(def at (atom {}))

(println @at)




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
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ["/test" {:get test-page}]])

