(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality item update-current-inventory]]))

(describe "gilded rose"
  (describe "item"
    (let [sample-item (item "Sample Item" 10 20)]
      (it "has a name"
        (should= (sample-item :name) "Sample Item"))

      (it "has a sell-in value"
        (should= (sample-item :sell-in) 10))

      (it "has a quality"
        (should= (sample-item :quality) 20))))

  (describe "update current inventory"
    (let [updated-inventory (update-current-inventory)]
      (let [
        dexterity-vest (nth updated-inventory 0)
        aged-brie (nth updated-inventory 1) elixir-mongoose (nth updated-inventory 2)
        sulfuras (nth updated-inventory 3) backstage-passes (nth updated-inventory 4)]

        (it "lowers the sell-in value at the end of the day for all items except 'Sulfuras'"
          (should= (dexterity-vest :sell-in) 9)
          (should= (aged-brie :sell-in) 1)
          (should= (elixir-mongoose :sell-in) 4)
          (should= (backstage-passes :sell-in) 14))

        (it "does not lower the sell-in value if the item is named 'Sulfuras, Hand of Ragnaros'"
          (should= (sulfuras :sell-in) 0))



      )))


)
