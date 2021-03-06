(ns myfirstclj.core
  (:use compojure.core
        ring.middleware.json
        ring.util.response)
  (:require [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [myfirstclj.view :as view]
            [compojure.core :refer [defroutes GET ANY]]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as jetty]
            [myfirstclj.views.layout :as layout]
            [myfirstclj.views.contents :as contents]
            [myfirstclj.controller.contentctlr :as controller]
            )
  )

;;https://www.youtube.com/watch?v=jOX0uK3jsbI&index=20&list=WL
(defn foo
  "I don't do a whole lot."
  [x]
  (str "Hello, "x))

(defroutes my_routes
  (GET "/" [] (view/callme) )
  (GET "/rest" [] (response {:email "abhishek.create@gmail.com"}))
  (route/resources "/")
  )

(comment(GET "/" [] (layout/application "Home" (contents/index))))
;search web page
(defroutes searchroutes
   controller/routes
   (route/resources "/")
   (ANY "*" [] (route/not-found (layout/application "Page Not Found" (contents/not-found))))
  (GET "/search" [] (layout/application "Search..." (contents/search-item)))
  (GET "/hello" [] (layout/application "Hello Boy" (contents/hello)))

  )

(def application (wrap-defaults searchroutes site-defaults))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (jetty/run-jetty application {:port port :join? false})))


;;search web page ends here

(def app
  (wrap-json-response my_routes))