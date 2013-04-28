(ns mydb.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [noir.util.middleware :as noir]
            [noir.response :as resp]))

(defroutes app-routes
  (GET "/" [] (resp/redirect "/html/main.html"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> [(handler/site app-routes)]
      noir/app-handler
      noir/war-handler))

