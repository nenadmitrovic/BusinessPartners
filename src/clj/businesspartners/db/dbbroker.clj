(ns businesspartners.db.dbbroker
  (:require [monger.core :as mongo]
            [monger.collection :as coll])
  (:import org.bson.types.ObjectId))

(def conn (mongo/connect))

(def db (mongo/get-db conn "business-partners"))

(defn save-partner [document]
  (try
    (-> document
        (merge {:_id (ObjectId.)})
        (#(monger.collection/insert-and-return db "partners" %))
        :_id
        .toString)
    (catch Exception e nil)))




(defn- transform-id-to-string [document]
  (if-let [id (:_id document)]
    (assoc document :_id (.toString id))))

(defn get-partner-by-id [^String id]
  (try
    (->> id
         ObjectId.
         (monger.collection/find-map-by-id db "partners")
         transform-id-to-string)
    (catch Exception e "no exist")))

(get-partner-by-id "123")

(defn get-all-partners []
  (map transform-id-to-string (monger.collection/find-maps db "partners")))

(get-all-partners)

(defn update-partner-by-id [id document]
  (try
    (-> document
        (#(monger.collection/update-by-id db "partners" (ObjectId. id) %))
        .getN)
    (catch Exception e -1)))

(update-partner-by-id
  "5e7d25c86d7abc269cc5e1f3"
  {:name "Nenad"
   :address "Main Street"
   :phone "555777"
   :email "nenadmitrovic@gmail.com"})





(defn delete-partner-by-id [^String id]
  (try
    (->> id
         ObjectId.
         (monger.collection/remove-by-id db "partners")
         .getN)
    (catch Exception e -1)))