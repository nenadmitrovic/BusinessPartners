(ns businesspartners.core
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure.string :as string]))


(def business-partner-id (r/atom nil))
(def business-partner (r/atom nil))



(defn save-business-partner [fields errors business-partners]
  (POST "/api/business-partner"
        {:format :json
         :headers
                 {"Accept" "application/transit+json"
                  "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(do
                     (reset! business-partners (:business-partners %))
                     (.alert js/window "You have successfully saved a new business partner!")
                     (reset! fields nil)
                     (reset! errors nil))
         :error-handler #(do
                           (.log js/console (str %))
                           (reset! errors (get-in % [:response :errors]))
                           (println "Errors from save business partner: " @errors))}))


(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.notification.is-danger (string/join error)]))

(defn add-business-partner [business-partners]
  (let [fields (r/atom {})
        errors (r/atom {})]
    (fn []
      [:div.container.mt-5.w-75
       [:div.text-center [:h2 "Add New Business Partner"]
        [:p "Fill in the form bellow to add a new business partner"]]
       [:div
        [errors-component errors :server-error]
        [:div.form-group
         [:label {:for :name} "Name"]
         [errors-component errors :name]
         [:input#name.form-control
          {:type :text
           :name :name
           :on-change #(swap! fields assoc :name (-> % .-target .-value))
           :value (:name @fields)
           }]]
        [:div.form-group
         [:label {:for :address} "Address"]
         [errors-component errors :address]
         [:input#address.form-control
          {:type :text
           :name :address
           :on-change #(swap! fields assoc :address (-> % .-target .-value))
           :value (:address @fields)
           }]]
        [:div.form-group
         [:label {:for :phone} "Phone"]
         [errors-component errors :phone]
         [:input#phone.form-control
          {:type :text
           :name :phone
           :on-change #(swap! fields assoc :phone (-> % .-target .-value))
           :value (:phone @fields)
           }
          ]]
        [:div.form-group
         [:label {:for :email} "Email"]
         [errors-component errors :email]
         [:input#email.form-control
          {:type :email
           :name :email
           :on-change #(swap! fields assoc :email (-> % .-target .-value))
           :value (:email @fields)
           }]]
        [:button.btn.btn-primary.btn-lg
         {:type :submit
          :on-click #(do
                       (save-business-partner fields errors business-partners))} "Save"]]])))

(defn get-partner-by-id [id]
  (GET "/api/get-partner-by-id"
       {:headers {"Accept" "application/transit+json"}
        :params {:id id}
        :handler (fn [arg]
                   (println :handler-arg arg)
                   (reset! business-partner (:business-partner arg))
                   (println "Business partner from handler: " @business-partner))}))


(defn get-business-partners [business-partners]
  (GET "/api/business-partners"
       {:headers {"Accept" "application/transit+json"}
        :handler #(reset! business-partners (:business-partners %))}))

(defn delete-business-partner [business-partners id]
  (POST "/api/delete-business-partner"
        {:headers {"Accept" "application/transit+json"}
         :format :json
         :params {:id id}
         :handler #(reset! business-partners (:business-partners %))}))



(defn business-partners-list [business-partners component]
  [:div.container.mt-5.w-100
   [:h2.text-center "Your business partners:"]
   [:table.table.table-hover.mt-4
    [:thead
     [:tr
      [:th "Name"]
      [:th "Address"]
      [:th "Phone"]
      [:th "Email"]]]
    [:tbody
     (for [{:keys [_id name address phone email]} @business-partners]
       [:tr
        [:td name]
        [:td address]
        [:td phone]
        [:td email]
        [:input.btn.btn-primary
          {:type :submit
           :value "Delete"
           :onClick #(let [answer (.confirm js/window "Are you sure you want to delete the business partner???")]
                       (if (= answer true)
                         (delete-business-partner business-partners _id)))}]
         [:input.btn.btn-primary.ml-2
          {:type :submit
           :value "Update"
           :onClick #(do (reset! business-partner-id _id)
                          (reset! component "update")
                          (get-partner-by-id _id))}]])]]])

(defn update-partner-by-id [id business-partner]
  (POST "/api/update-partner-by-id"
        {:headers {"Accept" "application/transit+json"}
         :params {:id id :business-partner business-partner}
         :handler #(println %)}))

(defn update-partner [one-partner component id]
  (fn []
    [:div.container.mt-5.w-75
     [:h2.text-center "Update Business Partner"]
     [:p.text-center "Fill in the form bellow to update the business partner"]
     [:div
      [:div.form-group
       [:label {:for :name} "Name"]
       [:input#name.form-control
        {:type :text
         :name :name
         :on-change #(swap! one-partner assoc :name (-> % .-target .-value))
         :value (:name @one-partner)
         }]]
      [:div.form-group
       [:label {:for :address} "Address"]
       [:input#address.form-control
        {:type :text
         :name :address
         :on-change #(swap! one-partner assoc :address (-> % .-target .-value))
         :value (:address @one-partner)
         }]]
      [:div.form-group
       [:label {:for :phone} "Phone"]
       [:input#phone.form-control
        {:type :text
         :name :phone
         :on-change #(swap! one-partner assoc :phone (-> % .-target .-value))
         :value (:phone @one-partner)
         }]]
      [:div.form-group
       [:label {:for :email} "Email"]
       [:input#email.form-control
        {:type :text
         :name :email
         :on-change #(swap! one-partner assoc :email (-> % .-target .-value))
         :value (:email @one-partner)
         }]]
      [:input.btn.btn-primary
       {:type :submit
        :value "Update"
        :onClick #(do
                    (update-partner-by-id id @one-partner)
                    (.alert js/window "You have successfully updated your business partner!")
                    (.reload js/window.location true))}]]]))

(defn home []
  (let [business-partners (r/atom nil)
        one-partner business-partner
        component (r/atom "")]
    (get-business-partners business-partners)

    (fn []
        (cond
          (= @component "")
              [:div.container
              [:div.container
               [add-business-partner business-partners]]
              [:div.container
               [business-partners-list business-partners component]]]
          (= @component "update")
              (update-partner one-partner component @business-partner-id)))))



(r/render
  [home]
   (.getElementById js/document "content"))





