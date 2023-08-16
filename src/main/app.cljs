(ns app)

(defn ^:export ^:dev/after-load init! []
  (println "Initialized" ::module))

(defn ^:export ^:dev/before-load halt! []
  (println "Halted" ::module))
