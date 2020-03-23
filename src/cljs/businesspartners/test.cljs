(ns businesspartners.test
  (:require [reagent.core :as r]))

(-> (.getElementById js/document "test")
    (.-innerHTML)
    (set! "Hello, Auto!"))







