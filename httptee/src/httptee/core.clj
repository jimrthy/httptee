(ns httptee.core
  (:use org.httpkit.server)
  (:gen-class))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "hello HTTP!"})

(defn -main
  [& args]
  ;; Really shouldn't hard-code this.
  (run-server app {:port 8080}))
