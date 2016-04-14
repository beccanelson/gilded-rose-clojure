(defproject gilded-rose "0.1.0-SNAPSHOT"
  :description "A Gilded Rose kata for Clojure"
  :url "http://github.com/beccanelson/gilded-rose-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"][speclj "2.2.0"]]}}

  :plugins [[speclj "2.2.0"]]
  :test-paths ["spec"])
