(ns gilded-rose.core-spec
 (:require [speclj.core :refer :all]
           [gilded-rose.core :refer :all]))

(def sulfuras (make-sulfuras 0 80))
(def default-item (item "Default Item" 10 20))
; (def updated-sell-in (update-sell-in default-item))
(def updated-quality (update-item-quality default-item))
(def updated-item (update-item default-item))

(def inventory [sulfuras])
(def updated-inventory (update-inventory inventory))

(describe "gilded rose"
  (context "item"

      (it "has a name"
        (should-not-be-nil (default-item :name)))

      (it "has a sell-in value"
        (should-not-be-nil (default-item :sell-in)))

      (it "has a quality"
        (should-not-be-nil (default-item :quality)))

     (context "Sulfuras"
        (it "has a name"
            (should= (sulfuras :name) "Sulfuras, Hand of Ragnaros"))))

  (context "#update-sell-in"
    (it "decreases the sell-in value"
      (let [updated-sell-in (update-sell-in default-item)]
        (should= (:sell-in updated-sell-in) 9))))

  (context "#update-quality"
    (it "decreases the quality"
      (should= (:quality updated-quality) 19)))

  (context "#update-item"
     (it "updates the quality and sell-in"
         (should= (:sell-in updated-item) 9)
         (should= (:quality updated-item) 19))

    (context "Sulfuras"

      (it "never decreases the quality or sell-in"
        (let [updated-sulfuras (update-item sulfuras)]
            (should= (:quality updated-sulfuras) 80)
            (should= (:sell-in updated-sulfuras) 0))))))

  ; (describe "update current inventory"
  ;   (let [updated-inventory (update-current-inventory)]
  ;     (let [
  ;           dexterity-vest (nth updated-inventory 0)
  ;           aged-brie (nth updated-inventory 1) elixir-mongoose (nth updated-inventory 2)
  ;           sulfuras (nth updated-inventory 3) backstage-passes (nth updated-inventory 4)]
  ;
  ;       (describe "default items"
  ;         (it "lowers the sell-in value by 1"
  ;           (should= (dexterity-vest :sell-in) 9)
  ;           (should= (aged-brie :sell-in) 1)
  ;           (should= (elixir-mongoose :sell-in) 4)
  ;           (should= (backstage-passes :sell-in) 14))
  ;
  ;         (it "lowers the quality by 1 if sell-in has not passed"
  ;           (should= (dexterity-vest :quality) 19)
  ;           (should= (elixir-mongoose :quality) 6))
  ;
  ;         (it "lowers the quality by 2 if sell-in has passed"
  ;           (let [old-dexterity-vest (item "+5 Dexterity Vest" 0 10)]
  ;             (do (update-quality [old-dexterity-vest])
  ;              (should= (old-dexterity-vest :quality) 8))))
  ;
  ;         (it "does not allow the quality to be greater than 50"
  ;           (let [super-aged-brie (item "Aged Brie" 0 50)]
  ;             (do (update-quality [super-aged-brie])
  ;              (should= (super-aged-brie :quality) 50))))
  ;
  ;         (it "does not allow the quality to be negative"
  ;           (let [old-elixir (item "Elixir of the Mongoose" 0 0)]
  ;             (do (update-quality [old-elixir])
  ;              (should= (old-elixir :quality) 0)))))
  ;
  ;
  ;       (describe "Aged Brie"
  ;         (it "increases in quality"
  ;           (should= (aged-brie :quality) 1)))))))


        ; (describe "Backstage passes"
        ;   ;these tests do not pass
        ;
        ;   (it "increases in quality as sell-in date approaches"
        ;     (should= (backstage-passes :quality) 21))
        ;   (it "increases by 2 when sell-in is 10 or less"
        ;     (let [backstage-passes-9 (item "Backstage passes to a TAFKAL80ETC concert" 9 25)]
        ;       (do (update-quality [backstage-passes-9])
        ;       (should= (backstage-passes-9 :quality) 27))))
        ;
        ;   (it "increases by 3 when there are 5 days or less"
        ;     (let [backstage-passes-3 (item "Backstage passes to a TAFKAL80ETC concert" 3 40)]
        ;       (do (update-quality [backstage-passes-3])
        ;       (should= (backstage-passes-3 :quality) 43))))
        ;
        ;   (it "decreases to 0 when sell-in has passed"
        ;     (let [backstage-passes-0 (item "Backstage passes to a TAFKAL80ETC concert" 0 50)]
        ;       (do (update-quality [backstage-passes-0])
        ;       (should= (backstage-passes-0 :quality) 0))))
        ; )
