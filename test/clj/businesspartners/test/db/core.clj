(ns businesspartners.test.db.core
  (:require [clojure.test :refer :all]
            [businesspartners.db.dbbroker :as d-b]))


(deftest test-business-partners
  (is (not (= (d-b/save-partner
                {:name "Nenad"
                 :address "Main Street"
                 :phone "555777"
                 :email "nenadmitrovic@gmail.com"})
              nil)))
  (is (= {:_id "5e7d24ee6d7abc1260fb0ba0"
          :name "Nenad Mitrovic"
          :address "Main Street"
          :phone "555777"
          :email "nenadmitrovic@gmail.com"}
         (d-b/get-partner-by-id "5e7d24ee6d7abc1260fb0ba0")))
  (is (= (d-b/update-partner-by-id
           "5e7d25c86d7abc269cc5e1f3"
           {:name "Sam"
            :address "1st Ave"
            :phone "333444"
            :email "sam@gmail.com"})
         1))
  (is (= 1 (d-b/delete-partner-by-id "5e7d297d6d7abc1260fb0ba9"))))


