(ns businesspartners.core
  (:require [reagent.core :as r]))

(defn add-business-partner []
  (let [fields (r/atom {})]
    (fn []
      [:div.container.mt-5.w-50
       [:div.text-center [:h2 "Add A New Business Partner"]
       [:p "Fill in the form bellow to add a new business partner"]]
       [:form {:action "/action_page"}
        [:div.form-group
         [:label {:for :name} "Name"]
         [:input#name.form-control
          {:type :text
           :name :name
           :on-change #(swap! fields assoc :name (-> % .-target .-value))
           :value (:name @fields)
           }]]
        [:div.form-group
         [:label {:for :address} "Adress"]
         [:input#address.form-control
          {:type :text
           :name :address
           :on-change #(swap! fields assoc :address (-> % .-target .-value))
           :value (:address @fields)
           }]]
        [:div.form-group
         [:label {:for :phone} "Phone"]
         [:input#phone.form-control
          {:type :text
           :name :phone
           :on-change #(swap! fields assoc :phone (-> % .-target .-value))
           :value (:phone @fields)
           }
          ]]
        [:div.form-group
         [:label {:for :email} "Email"]
         [:input#email.form-control
          {:type :email
           :name :email
           :on-change #(swap! fields assoc :email (-> % .-target .-value))
           :value (:email @fields)
           }]]
        [:button.btn.btn-primary.btn-lg {:type :submit} "Save"]]])))

(defn home []
  [add-business-partner])

(r/render
  [home]
  (.getElementById js/document "content"))
