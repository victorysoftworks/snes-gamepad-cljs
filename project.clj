(defproject snes-gamepad "0.9.0"
  :description "A ClojureScript library providing a simple interface for using USB SNES gamepads in HTML5 game projects."
  :url "https://github.com/victorysoftworks/snes-gamepad-cljs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.521"]]
  :plugins [[lein-cljsbuild "1.1.6"]]
  :cljsbuild {
    :builds [{:source-paths ["src/cljs"]
              :compiler {:main 'snes-gamepad.gamepad
                         :output-to "resources/public/core.js"
                         :output-dir "resources/public/"
                         :optimizations :whitespace}}]})
