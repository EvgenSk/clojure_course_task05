(ns main
  (:require [enfocus.core :as ef]
            [clojure.browser.repl :as repl]
            [clojure.string :as str])
  (:require-macros [enfocus.macros :as em])
  (:use [jayq.core :only [$ css inner]]))

;; (repl/connect "http://localhost:9000/repl")

(def my-vals (atom []))

(em/defsnippet input-button "/html/main.html" [:#input-button]
  []
  [:#input-button] (em/set-attr :style ""))

(defn make-table [{:keys [header content]}]
  (str "<table><tr><th>" header "</th></tr>" content "</table>"))

(defn make-row [content]
  (str "<tr><td>" content "</td></tr>"))

(def -input-button
  "<div id=\"input-button\">
      <input id=\"mytext\" type=\"text\"></input><br/>
      <a href=\"#\" onclick=\"main.add_to_list();\">Add to list</a>
    </div>")

(defn show-list []
  (em/at js/document
         [:#insert-here] (em/content (str/join " " [(make-table {:header "Just a list"
                                                                :content (str/join " " (map make-row @my-vals))})
                                                    -input-button]))))

(defn ^:export add-to-list []
  (let [data (em/from js/document
                      :myvalue [:#mytext] (em/get-prop :value))]
    (do
      (swap! my-vals conj (:myvalue data))
      (show-list))))

(defn start []
  (show-list))

(set! (.-onload js/window) start)
