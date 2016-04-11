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

        (describe "default items"
          (it "lowers the sell-in value by 1"
            (should= (dexterity-vest :sell-in) 9)
            (should= (aged-brie :sell-in) 1)
            (should= (elixir-mongoose :sell-in) 4)
            (should= (backstage-passes :sell-in) 14))

          (it "lowers the quality by 1 if sell-in has not passed"
            (should= (dexterity-vest :quality) 19)
            (should= (elixir-mongoose :quality) 6))
          )


        (describe "Aged Brie"
          (it "increases in quality"
            (should= (aged-brie :quality) 1))
        )

        (describe "Sulfuras"
          (it "never lowers it sell-in value"
            (should= (sulfuras :sell-in) 0))

          (it "never decreases in quality"
            (should= (sulfuras :quality) 80))
        )
))))
