(ns todolist.core
  (:require [todolist.item.model :as items]
            [todolist.item.handler :refer [handle-index-items
                                           handle-create-item
                                           handle-delete-item
                                           handle-update]]
            
       
            ;; ring.adapter.jetty is an adapter we can use in dev and production
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            ;; wrap-params to parse the query-string and store the result 
            ;; back into the request
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            ;; handle-dump helps us to see the request in a nice format
            ;; It's useful as a debugging tool
            [ring.handler.dump :refer [handle-dump]]))

(def db (or (System/getenv "DATABASE_URL")
            "jdbc:postgresql://localhost/todolist"))

(defroutes routes
  (GET "/" [] handle-index-items)
  (POST "/" [] handle-create-item)
  (DELETE "/:item-id" [] handle-delete-item)
  (PUT "/:action/:item-id" [] handle-update)
  (not-found "Page not found"))

(defn wrap-db [hdlr]
  (fn [req]
    (hdlr (assoc req :webdev/db db))))

(def sim-methods {"PUT" :put
                  "DELETE" :delete})

(defn wrap-simulated-method [hdlr]
  (fn [req]
    (if-let [method (and (= :post (:request-method req))
                         ;; Si se cumple esto el resultado es true
                         (sim-methods (get-in req [:params "_method"])))]
                         ;; De aquí sacamos "PUT" o "DELETE" y es el valor 
                         ;; que asquiere method si lo anterior es true
      (hdlr (assoc req :request-method method))
      ;;Aquí es donde hacemos el truco. Modificamos el :request-method de
      ;; :post a :put o :delete
      (hdlr req))))

(def app
  (wrap-file-info
   (wrap-resource
    (wrap-db
     (wrap-params
      (wrap-simulated-method
       routes)))
    "static")))

(defn -main [port]
  (items/create-table db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))


