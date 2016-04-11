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
        (should= (sample-item :quality) 20))))

    (describe "update quantity"
      (let [any-item (item "Default Item" 10 20) sulfuras (item "Sulfuras, Hand of Ragnaros" 0 80)]

        (update-quality [any-item sulfuras])

        (it "lowers the sell-in value at the end of the day"
          (should= (any-item :sell-in) 9))

        (it "does not lower the sell-in value if the item is named 'Sulfuras, Hand of Ragnaros'"
          (should= (sulfuras :sell-in) 0))))

)
