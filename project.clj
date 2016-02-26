(defproject todolist "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; [ring "1.4.0"] includes all 4 ring libraries:
                 ;; ring-core, ring devel, ring-servlet and ring-jetty-adapter
                 [ring "1.4.0"]
                 [compojure "1.4.0"]
                 ;; clojure.java.jdbc is a wrapper for JDBC-based access to DB.
                 [org.clojure/java.jdbc "0.4.2"]
                 [postgresql/postgresql "9.1-901-1.jdbc4"]
                 [hiccup "1.0.5"]]
  ;; We need to specify we want at least leiningen 2 wich has better
  ;; support for Heroku
  :min-lein-version "2.0.0"

  ;; We also need to specify the name of the jar file. When we deploy,
  ;; heroku will build  this file for us. It will include all dependencies.
  :uberjar-name "todolist.jar"
  :main todolist.core
  :profiles {:dev
             {:main todolist.core/-dev-main}})

