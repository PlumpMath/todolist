(ns todolist.item.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [html h]]))

(defn update-checked-form [id checked]
  (html [:form {:method "POST" :action (str "/check/" id)}
         ;; Browsers do not support DELETE methods in html forms
         ;; They do support it in AJAX, but we are not using AJAX now
         ;; We use a hidden input to pass the simulated "PUT" method to our
         ;; sim-methods wrapper
         [:input {:type :hidden
                  :name "_method"
                  :value "PUT"}]
         [:input {:type :hidden
                  :name "checked"
                  :value (if checked "false" "true")}]
         [:div.btn-group
          [:button.btn.btn-xs
           (if checked [:span.glyphicon.glyphicon-check] [:span.glyphicon.glyphicon-unchecked] )]]]))

(defn update-priority-form [id priority]
  (html [:form {:method "POST" :action (str "/prior/" id)}
         [:input {:type :hidden
                  :name "_method"
                  :value "PUT"}]
         [:input {:type :hidden
                  :name "priority"
                  :value (if priority "false" "true")}]
         [:div.btn-group
          [:button.btn.btn-xs
           (if priority [:span.glyphicon.glyphicon-star] [:span.glyphicon.glyphicon-star-empty] )]]]))

(defn update-checked [text checked]
  (if checked
    [:del text]
    text))

(defn delete-form [id]
  (html [:form
         {:method "POST" :action (str "/" id)}
         [:input {:type :hidden
                  :name "_method"
                  :value "DELETE"}]
         [:div.btn-group
          [:button.btn.btn-danger.btn-xs
           [:span.glyphicon.glyphicon-trash]]]]))


(defn items-page [items]
  (html5 {:lang :en}
         [:head
          [:title "Mis tareas"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]
          [:style "* {box-sizing: border-box}"]]
         [:body
          [:div.container
           [:div.jumbotron.text-center 
            [:h1 "Mis tareas"]]]
          [:div.container
           [:div.row
            [:table.table.table-striped
             [:thead
              [:tr
               [:th.col-sm-1.text-center "Hecho"]
               [:th.col-sm-1.text-center [:span.glyphicon.glyphicon-star-empty]]
               [:th.col-sm-1.text-center "Tag"]
               [:th.col-sm-8 "Tarea"]
               [:th.col-sm-1.text-center [:span.glyphicon.glyphicon-trash]]]]
             [:tbody
              (for [i items]
                [:tr
                 [:td.text-center (update-checked-form (:id i) (:checked i))]
                 [:td.text-center (update-priority-form (:id i) (:priority i))]
                 [:td.text-center "tag"]
                 [:td (update-checked (:description i) (:checked i))]
                 [:td.text-center (delete-form (:id i))]])
              ]]]
           [:div.row
            [:table.table
             [:tbody
              [:tr
               [:form.form-inline
                {:method "POST" :action "/"}
                [:td.col-sm-1.text-center [:div.form-group
                                           [:input.btn.btn-success.btn-xs
                                            {:type :submit
                                             :value "Nueva Tarea"}]]]
                [:td.col-sm-1.text-center "tag"]
                [:td.col-sm-10.text-center [:div.form-group
                                            [:label.control-label.sr-only {:for :desc-input} "Tarea"]
                                            ;; sr.only es para que el label no se vea. Es conveniente añadir
                                            ;; siempre un label para cada input
                                            [:input#desc-input.form-control
                                             ;; .form-control hace que el input tenga el 100% de ancho
                                             {:name :description
                                              :placeholder "Escribe aquí para añadir una nueva tarea"}]]]]]]]]]

          [:script {:src "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"}]
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))


#_(defn new-item []
  (html
   [:form.form-inline
    {:method "POST" :action "/"}
    [:td.col-sm-1.text-center
     [:div.form-group
      [:input.btn.btn-success.btn-xs
       {:type :submit
        :value "Nueva Tarea"}]]]
    [:td.col-sm-1.text-center ""]
    [:td.col-sm-1.text-center ""]
    [:td.col-sm-9.text-center
     [:div.form-group
      [:label.control-label.sr-only {:for :desc-input} "Tarea"]
      [:input#desc-input.form-control
       {:name :description
        :placeholder "Escribe aquí para añadir una nueva tarea"}]]]
    
    ]))
