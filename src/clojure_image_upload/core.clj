(ns clojure-image-upload.core
    (:require [ring.adapter.jetty :as j]
            [compojure.core :as c]
            [hiccup.core :as h]
            [ring.middleware.params :as p]
            [ring.util.response :as r])
  
  (:gen-class))

(defonce pictures (atom[]))
(defonce server (atom nil))

(add-watch pictures :save-to-disk
  (fn [_ _ _ _]
    (spit "pictures.edn" (pr-str @pictures))))

(c/defroutes app
  (c/GET "/" request
   (h/html [:html
            [:body
             [:form {:action "/add-picture" :enctype "multipart/form-data" :method "post"}
              [:input {:type "file" :placeholder "Upload an image" :name "picture"}]
              [:button {:type "Submit"} "Upload Photo"]]
             [:ul
              (map (fn [picture]
                     [:li pictures])
                @pictures)]]]))

  (c/POST "/add-picture" request
    (let [picture (get (:params request) "picture")]
      (swap! pictures conj picture)
      (r/redirect "/"))))

(defn -main []
  (try
    (let [pictures-str (slurp "pictures.edn")
          pictures-vec (read-string pictures-str)]
      (reset! pictures pictures-vec))
    (catch Exception _))
  
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty (p/wrap-params app) {:port 8080 :join? false})))
