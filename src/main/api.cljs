(ns api
  (:require
   [macchiato.http :as http]
   [macchiato.middleware.defaults :refer [site-defaults
                                          wrap-defaults]]
   [macchiato.util.response :as r]
   ["cowsay" :refer (say)]))

(defn page-handler [req respond raise]
  (println "normal request to" (get req :url))
  (-> (r/ok "would normally send html")
      (r/content-type "text/plain")
      respond))

(defn api-handler [req respond raise]
  (js/console.debug "API Endpoint Hit (debug)")
  (js/console.log "API Endpoint Hit (log)")
  (js/console.info "API Endpoint Hit (info)")
  (js/console.warn "API Endpoint Hit (warn)")
  (js/console.error "API Endpoint Hit (error)")
  (let [name    (get-in req [:query-params "name"] "friend")
        version js/process.version
        url     (get req :url)
        time    (.toISOString (js/Date.))
        text    (str "Howdy, " name ", from Vercel!\n"
                     "Node.js: " version "\n"
                     "Request URL: " url "\n"
                     "Server time: " time)
        body    (say #js {:text text})]
    (println "about to return body:\n" body)
    (-> (r/ok body)
        (r/content-type "text/plain")
        respond)
    (println "response sent")))

(defn handler [req respond raise]
  (let [h (if (get-in req [:query-params "full_non_api_path"])
            page-handler
            api-handler)]
    (h req respond raise)))

(def ^:export main
  (-> handler
      (wrap-defaults site-defaults)
      (http/handler {})))
