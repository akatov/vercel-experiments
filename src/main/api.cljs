(ns api
  (:require ["cowsay" :refer (say)]))

(defn ^:export main [^js/Request req ^js/Response res]
  (let [name    (or (.. req -query -name) "friend")
        version js/process.version
        url     (.-url req)
        time    (.toISOString (js/Date.))
        text    (str "Howdy, " name ", from Vercel!\n"
                     "Node.js: " version "\n"
                     "Request URL: " url "\n"
                     "Server time: " time)
        body    (say #js {:text text})]
    (doto res
      (.setHeader "Content-Type" "text/plain")
      (.end body)))
  js/undefined)
