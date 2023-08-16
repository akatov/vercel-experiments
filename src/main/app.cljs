(ns app)

(defn ^:export ^:dev/after-load init! []
  (js/console.log "Hello World!")
  (println "Initialized" ::module))

(defn ^:export ^:dev/before-load halt! []
  (println "Halted" ::module))

