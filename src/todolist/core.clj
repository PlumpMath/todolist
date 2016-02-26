(ns todolist.core
  (:require 
            
       
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

(defn greet [req]
  {:status 200
   :body "Hello, world!"
   :headers {}})

(defroutes app
  (GET "/" [] greet)
  (not-found "Page not found"))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
