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
    (catch Exception e)))

(defn get-all-partners []
  (map transform-id-to-string (monger.collection/find-maps db "partners")))

(defn update-partner-by-id [^String id document collection]
  (try
    (-> document
        (dissoc :_id)
        (#(monger.collection/update-by-id db collection (ObjectId. id) %))
        .getN)
    (catch Exception e -1)))

(defn delete-partner-by-id [^String id]
  (try
    (->> id
         ObjectId.
         (monger.collection/remove-by-id db "partners")
         .getN)
    (catch Exception e -1)))