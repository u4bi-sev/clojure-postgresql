(ns c-pg.core
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.handler :as h]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]))

(defroutes handler
           (context "/user" []
                    (GET "/" [] (response {:message "get users"})) 
                    (GET "/:id" [id] (response {:message (str "get user id - " id)})) 
                    (POST "/" [name pay age] (response {:message (str "insert user - " name " / " pay " / " age)}))
                    (PUT "/:id" [id] (response {:message (str "update user id - " id)})) 
                    (DELETE "/:id" [id] (response {:message (str "delete id - " id)})))
           (route/not-found (response {:message "not found"})))

(def app
  (wrap-json-response 
   (h/api handler)))