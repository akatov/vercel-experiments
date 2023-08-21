(ns api
  (:require
   [macchiato.http :as http]
   [macchiato.middleware.defaults :refer [site-defaults
                                          wrap-defaults]]
   [macchiato.util.response :as r]
   [reagent.dom.server :as rds]

   ["cowsay" :refer (say)]
   ["@heroicons/react/20/solid" :refer (ChevronRightIcon)]))

(defn html-component [name]
  (let [greeting (str "Hello, " name)]
    [:html
     [:head
      [:title greeting]
      [:script {:src "https://cdn.tailwindcss.com"}]]
     [:body
      [:div {:class "relative isolate overflow-hidden bg-white"}
       [:svg
        {:class "absolute inset-0 -z-10 h-full w-full stroke-gray-200 [mask-image:radial-gradient(100%_100%_at_top_right,white,transparent)]",
         :aria-hidden "true"}
        [:defs
         [:pattern
          {:id "0787a7c5-978c-4f66-83c7-11c213f99cb7",
           :width 200,
           :height 200,
           :x "50%",
           :y -1,
           :pattern-units "userSpaceOnUse"}
          [:path {:d "M.5 200V.5H200", :fill "none"}]]]
        [:rect
         {:width "100%",
          :height "100%",
          :stroke-width "{0}",
          :fill "url(#0787a7c5-978c-4f66-83c7-11c213f99cb7)"}]]
       [:div
        {:class
         "mx-auto max-w-7xl px-6 pb-24 pt-10 sm:pb-32 lg:flex lg:px-8 lg:py-40"}
        [:div
         {:class
          "mx-auto max-w-2xl lg:mx-0 lg:max-w-xl lg:flex-shrink-0 lg:pt-8"}
         [:img
          {:class "h-11",
           :src
           "https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600",
           :alt "Your Company"}]
         [:div
          {:class "mt-24 sm:mt-32 lg:mt-16"}
          [:a {:href "#", :class "inline-flex space-x-6"}
           [:span {:class "rounded-full bg-indigo-600/10 px-3 py-1 text-sm font-semibold leading-6 text-indigo-600 ring-1 ring-inset ring-indigo-600/10"}
            "What's new"]
           [:span {:class "inline-flex items-center space-x-2 text-sm font-medium leading-6 text-gray-600"}
            [:span "Just shipped v1.0"]
            [:> ChevronRightIcon {:class "h-5 w-5 text-gray-400", :aria-hidden "true"}]]]]
         [:h1 {:class "mt-10 text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl"}
          "Deploy to the cloud with confidence"]
         [:p {:class "mt-6 text-lg leading-8 text-gray-600"}
          "Anim aute id magna aliqua ad ad non deserunt sunt. Qui irure qui lorem cupidatat commodo. Elit sunt amet\n            fugiat veniam occaecat fugiat aliqua."]
         [:div {:class "mt-10 flex items-center gap-x-6"}
          [:a {:href "#", :class "rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"}
           "Get started"]
          [:a {:href "#", :class "text-sm font-semibold leading-6 text-gray-900"}
           "Learn more"
           [:span {:aria-hidden "true"} "→"]]]]
        [:div {:class "mx-auto mt-16 flex max-w-2xl sm:mt-24 lg:ml-10 lg:mr-0 lg:mt-0 lg:max-w-none lg:flex-none xl:ml-32"}
         [:div {:class "max-w-3xl flex-none sm:max-w-5xl lg:max-w-none"}
          [:div {:class "-m-2 rounded-xl bg-gray-900/5 p-2 ring-1 ring-inset ring-gray-900/10 lg:-m-4 lg:rounded-2xl lg:p-4"}
           [:img
            {:src "https://tailwindui.com/img/component-images/project-app-screenshot.png",
             :alt "App screenshot",
             :width 2432,
             :height 1442,
             :class "w-[76rem] rounded-md shadow-2xl ring-1 ring-gray-900/10"}]]]]]]]]))

(defn page-handler [req respond raise]
  (println "normal request to" (get req :url))
  (-> [html-component "Dmitri"]
      rds/render-to-string
      r/ok
      (r/content-type "text/html")
      respond)
  )

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
