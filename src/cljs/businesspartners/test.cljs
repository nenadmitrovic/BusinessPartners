(ns businesspartners.test)

(defn test []
  (-> (.getElementById js/document "test")
      (.-innerHTML)
      (set! "Hello, World!")))

(defn ^:export init []
  (test))



