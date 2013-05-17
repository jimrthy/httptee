(ns httptee.core
  (:use org.httpkit.server)
  (:require [org.httpkit.client :as http])
  (:gen-class))

(defn -parse-headers [header-string]
  (throw (Throwable. "Not Implemented")))

(defn -parse [req]
  "Converts the REQUEST map into the sort of map that makes sense to me"
  (let [method-map {:get http/get} ;; other correlations seem obvious...prove through experiment
        header-map (-parse-headers (req :headers))]
    (let [result {:method (method-map (req :request-method))
                  :user-agent (:user-agent header-map)
                  :uri (req :uri)}])))

(defn app [req]
  "Proxy the request to multiple backends.
Useful for comparing results and slowly moving new functionality into production"
  ;; Most of these honestly should be parsed from request
  (println req)
  
  ;; OK, req is a map.
  ;; Potentially Interesting keys:
  ;; :request-method (initially :get)
  ;; :query-string
  ;; :uri
  ;; :server-name
  ;; :headers (esp. the host and user-agent
  ;; :server-port
  ;; :body
  (let* [request (-parse req)
         options {:timeout 60000 ; ms
                  :basic-auth ["user" "pass"]
                  :user-agent (:user-agent request)
                  }
         resp ((request :method) (request :uri) options)
         resp-secondary ((request :method) (request :uri) options)
         response @resp]
        (println request)
        (println "Main Response: " response)
        (println "Secondary Response: " @resp-secondary)
        response))

(defn -main
  [& args]
  ;; Really shouldn't hard-code this.
  ;; The annoying piece of this API is that run-server returns its own stop function.
  ;; So we have to capture that.
  (run-server app {:port 8080}))
