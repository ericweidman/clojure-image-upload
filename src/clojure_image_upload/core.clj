(ns clojure-image-upload.core
    (:require [ring.adapter.jetty :as j]
            [compojure.core :as c]
            [hiccup.core :as h]
            [ring.middleware.params :as p]
            [ring.util.response :as r])
  
  (:gen-class))

(defonce pictures (atom[]))
(defonce server (atom nil))

(add-watch messages :save-to-disk
  (fn [_ _ _ _]
    (spit "pictures.edn" (pr-str @pictures))))


(defn -main []
  (println "Hello, world!"))
