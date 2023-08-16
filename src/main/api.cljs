(ns api
  (:require ["cowsay" :refer (say)]))

(defn ^:export main [^js/Request req ^js/Response res]
  (js/console.debug "API Endpoint Hit (debug)")
  (js/console.log "API Endpoint Hit (log)")
  (js/console.info "API Endpoint Hit (info)")
  (js/console.warn "API Endpoint Hit (warn)")
  (js/console.error "API Endpoint Hit (error)")
  (println "request:" req)
  (let [name    (or (.. req -query -name) "friend")
        version js/process.version
        url     (.-url req)
        time    (.toISOString (js/Date.))
        text    (str "Howdy, " name ", from Vercel!\n"
                     "Node.js: " version "\n"
                     "Request URL: " url "\n"
                     "Server time: " time)
        body    (say #js {:text text})]
    (println "about to return body:\n" body)
    (doto res
      (.setHeader "Content-Type" "text/plain")
      (.end body)))
  js/undefined)
