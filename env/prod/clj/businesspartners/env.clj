(ns businesspartners.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[businesspartners started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[businesspartners has shut down successfully]=-"))
   :middleware identity})
