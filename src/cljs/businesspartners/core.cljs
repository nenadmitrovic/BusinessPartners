(ns businesspartners.core
  (:require [reagent.core :as r]))

(defn add-business-partner []
  (let [fields (r/atom {})]
    (fn []
      [:div.container {:class "mt-4"}
       [:div.text-center [:h2 "Add New Business Partner"]
       [:p "Fill in the form bellow to add new business partner"]]
       [:form {:action "/action_page"}
        [:div.form-group
         [:label {:for "name"} "Name"]
         [:input#name.form-control {:type "text" :name "name"}]]
        [:div.form-group
         [:label {:for "adress"} "Adress"]
         [:input#adress.form-control {:type "text" :name "adress"}]]
        [:div.form-group
         [:label {:for "phone"} "Phone"]
         [:input#phone.form-control {:type "text" :name "phone"}]]
        [:div.form-group
         [:label {:for "email"} "Email"]
         [:input#email.form-control {:type "email" :name "email"}]]
        [:button.btn.btn-primary.btn-lg {:type "submit"} "Save"]]])))

(defn home []
  [add-business-partner])

(r/render
  [home]
  (.getElementById js/document "content"))
