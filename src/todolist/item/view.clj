(ns todolist.item.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [html h]]))

(defn new-item []
  (html
   [:form.form-horizontal
    {:method "POST" :action "/"}
    [:div.form-group
     [:label.control-label.col-sm-2 {:for :desc-input} "Tarea"]
     [:div.col-sm-10
      [:input#desc-input.form-control
       {:name :description
        :placeholder "Tarea"}]]]
    [:div.form-group
     [:div.col-sm-offset-2.col-sm-10
      [:input.btn.btn-primary
       {:type :submit
        :value "Nueva Tarea"}]]]]))

(defn update-checked-form [id checked]
  (html [:form {:method "POST" :action (str "/" id)}
         ;; Browsers do not support DELETE methods in html forms
         ;; They do support it in AJAX, but we are not using AJAX now
         ;; We use a hidden input to pass the simulated "PUT" method to our
         ;; sim-methods wrapper
         [:input {:type :hidden
                  :name "_method"
                  :value "PUT"
                  }]
         [:input {:type :hidden
                  :name "checked"
                  :value (if checked "false" "true")}]
         [:div.btn-group
          [:button.btn.btn-primary.btn-xs
           (if checked "Hecho" "Por hacer")]]]))

(defn delete-form [id]
  (html [:form
         {:method "POST" :action (str "/" id)}
         [:input {:type :hidden
                  :name "_method"
                  :value "DELETE"}]
         [:div.btn-group
          [:input.btn.btn-danger.btn-xs
           {:type :submit
            :value "Borrar"}]]]))


(defn items-page [items]
  (html5 {:lang :en}
         [:head
          [:title "Mis tareas"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]]
         [:body
          [:div.container
           [:h1 "Mis tareas"]
           [:div.row
            (if (seq items)
              [:table.table.table-striped
               [:thead
                [:tr
                 [:th.col-sm-1 "Hecho"]
                 [:th.col-sm-1 [:span.glyphicon.glyphicon-star-empty]]
                 [:th.col-sm-1 "Tag"]
                 [:th "Tarea"]
                 [:th [:span.glyphicon.glyphicon-trash]]]]
               [:tbody
                (for [i items]
                  [:tr
                   [:td (update-checked-form (:id i) (:checked i))]
                   [:td #_(update-priority-form (:id i) (:priority i))]
                   [:td "tag"]
                   [:td (:description i)]
                   [:td (delete-form (:id i))]])]]
              [:div.col-sm-offset-1 "No hay tareas."])]
           [:div.col-sm-6
            [:h3 "Crear una nueva tarea"]
            (new-item)]]
          [:script {:src "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"}]
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))
