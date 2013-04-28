(ns main
  (:require [enfocus.core :as ef]
            [clojure.browser.repl :as repl])
  (:require-macros [enfocus.macros :as em])
  (:use [jayq.core :only [$ css inner]]))

;; (repl/connect "http://localhost:9000/repl")

(def my-vals (atom []))

(em/defsnippet input-button "/html/main.html" [:#input-button]
  []
  [:#input-button] (em/set-attr :style ""))

(em/defsnippet tablefrag "/html/main.html" [:#mytable]
  [{:keys [header content]}]
  [:#mytable] (em/do->(em/set-attr :style "")
                      (em/append content))
  [:th] (em/content header))

(em/defsnippet rowfrag "/html/main.html" [:#my-row]
  [param]
  [:#my-td] (em/content param))

(defn show-list []
  (em/at js/document
         [:#insert-here] (em/do-> (em/content (tablefrag {:header "Just a list"
                                                          :content (map rowfrag @my-vals)}))
                                  (em/append (input-button)))))

(defn ^:export add-to-list []
  (let [data (em/from js/document
                      :myvalue [:#mytext] (em/get-prop :value))]
    (do
      (swap! my-vals conj (:myvalue data))
      (show-list))))

(defn start []
  (show-list))

(set! (.-onload js/window) start)

