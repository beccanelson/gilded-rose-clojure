(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality item]]))

(describe "gilded rose"
  (describe "item"
    (let [sample-item (item "Sample Item" 10 20)]
      (it "has a name"
        (should= (sample-item :name) "Sample Item"))
      (it "has a sell-in value"
        (should= (sample-item :sell-in) 10))
      (it "has a quality"
        (should= (sample-item :quality) 20)))

))
