(ns businesspartners.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [businesspartners.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[businesspartners started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[businesspartners has shut down successfully]=-"))
   :middleware wrap-dev})
