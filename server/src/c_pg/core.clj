(ns c-pg.core
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.handler :as h]
            [compojure.route :as route]
            [ring.middleware.json :as m-json]
            [clojure.java.jdbc :as sql]
            [ring.middleware.cors :refer [wrap-cors]]))

(def conn {:dbtype "postgresql"
           :dbname "postgres"
           :host "localhost"
           :port 5432})

(defn response-data [data]
  (cond
    (identity data) data
    :else {:message "data not found"}))

(defn get-users []
  (sql/query conn
             ["SELECT * FROM _user"]))

(defn get-user [id]
  (sql/query conn 
             ["SELECT 
                  *
                FROM 
                    _user
                WHERE 
                    id = ?" id]
             {:result-set-fn first}))

(defn insert-user [name pay age]
  (first
   (sql/insert! conn
                :_user {
                        :name name
                        :pay pay
                        :age age})))

(defn update-user [id name pay age]
  (let [is-updated (first
                    (sql/update! conn
                                 :_user
                                 {
                                  :name name
                                  :pay pay
                                  :age age}
                                 ["id = ?" id]))]
    (if is-updated {
                    :id id
                    :name name
                    :pay pay
                    :age age} nil)))

(defn delete-user [id]
  (let [is-deleted (first
                    (sql/delete! conn
                                 :_user
                                 ["id = ?" id]))]
    (if is-deleted {:id id} nil)))

(defroutes handler
           (context "/user" []
                    (GET "/" [] (response (response-data (get-users))))
                    (GET "/:id" [id] (response (response-data (get-user (read-string id)))))
                    (POST "/" [name pay age] (response (response-data (insert-user name pay age))))
                    (PUT "/:id" [id name pay age] (response (response-data (update-user (read-string id) name pay age))))
                    (DELETE "/:id" [id] (response (response-data (delete-user (read-string id))))))
           (route/not-found (response {:message "not found"})))

(def app
  (-> (h/api handler)
      (m-json/wrap-json-params)
      (m-json/wrap-json-response)
      (wrap-cors :access-control-allow-origin [#"http://127.0.0.1:5500"] ; #".*"
                 :access-control-allow-methods [:get :put :post :delete])))