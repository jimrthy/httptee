(ns httptee.core
  (:use org.httpkit.server)
  (:require [org.httpkit.client :as http])
  (:gen-class))

(defn app [req]
  "Proxy the request to multiple backends.
Useful for comparing results and slowly moving new functionality into production"
  ;; Most of these honestly should be parsed from request
  (println req) 
  (let* [options {:timeout 60000 ; ms
                  :basic-auth ["user" "pass"]
                  :user-agent "User-Agent-string"
                  }
         resp (http/get "http://main")
         resp-secondary (http/get "http://secondary")
         response @resp]
        (println "Main Response: " response)
        (println "Secondary Response: " @resp-secondary)
        response))

(defn -main
  [& args]
  ;; Really shouldn't hard-code this.
  ;; The annoying piece of this API is that run-server returns its own stop function.
  ;; So we have to capture that.
  (run-server app {:port 8080}))
