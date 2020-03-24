(ns businesspartners.core
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure.string :as string]))


(def component (r/atom ""))



(defn save-business-partner [fields errors business-partners]
  (POST "/api/business-partner"
        {:format :json
         :headers
                 {"Accept" "application/transit+json"
                  "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(do
                     (reset! business-partners (:business-partners %))
                     (reset! fields nil)
                     (reset! errors nil))
         :error-handler #(do
                           (.log js/console (str %))
                           (reset! errors (get-in % [:response :errors])))}))

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
          :on-click #(save-business-partner fields errors business-partners)} "Save"]]])))

(defn add-update-partner [business-partners]
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
          :on-click #(save-business-partner fields errors business-partners)} "Save"]]])))

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



(defn business-partners-list [business-partners]
  (println (str "Moji biznis partneri: " business-partners))
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
           :onClick #(delete-business-partner business-partners _id)}]
         [:input.btn.btn-primary.ml-2
          {:type :submit
           :value "Update"
           :onClick #(reset! component "update")}]])]]])


(defn update-form []
  [:div.container
   [:h1 "Welcome to update form"]
   [:input.btn.btn-primary
    {:type :submit
     :value "Start"
     :onClick #(reset! component "")}]])

(defn home []
  (let [business-partners (r/atom nil)]
    (get-business-partners business-partners)
    (fn []
      (if (= @component "")
        [:div.container
         [:div.container
          [add-business-partner business-partners]]
         [:div.container
          [business-partners-list business-partners]]]
        [:div.container
         [update-form]]))))



(r/render
  [home]
  (.getElementById js/document "content"))

