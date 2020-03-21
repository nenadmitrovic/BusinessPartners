(ns businesspartners.core
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure.string :as string]))

(defn save-business-partner [fields errors]
  (POST "/save-business-partner"
        {:format :json
         :headers
                 {"Accept" "application/transit+json"
                  "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(do
                     (.log js/console (str "response:" %))
                     (reset! errors nil))
         :error-handler #(do
                           (.log js/console (str %))
                           (reset! errors (get-in % [:response :errors])))}))

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.notification.is-danger (string/join error)]))

(defn add-business-partner []
  (let [fields (r/atom {})
        errors (r/atom {})]
    (fn []
      [:div.container.mt-5.w-50
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
          :on-click #(save-business-partner fields errors)} "Save"]]])))



(defn home []
  [add-business-partner])

(r/render
  [home]
  (.getElementById js/document "content"))
