(ns gilded-rose.core-spec
 (:require [speclj.core :refer :all]
           [gilded-rose.core :refer :all]))

(def default-item (item "Default Item" 10 20))
(def sulfuras (make-sulfuras "Sulfuras, Hand of Ragnaros" 0 80))
(def passes (make-passes "Backstage passes to a TAFKAL80ETC concert" 15 20))
(def brie (make-brie "Aged Brie" 2 0))
(def elixir (item "Elixir of the Mongoose" 5 7))
(def vest (item "+5 Dexterity Vest" 10 20))

(def updated-quality (update-quality default-item))
(def updated-item (update-item default-item))

(def inventory [vest elixir brie sulfuras passes])
(def updated-inventory (update-inventory inventory))

(defn find-by-item [item coll]
  (first (filter #(= (% :item) item) coll)))

(defn find-by-name [name coll]
  (first (filter #(= (% :name) name) coll)))

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
            (should= (sulfuras :name) "Sulfuras, Hand of Ragnaros"))

        (it "has a sell-in value"
            (should-not-be-nil (sulfuras :sell-in)))

        (it "has a quality"
            (should-not-be-nil (sulfuras :quality))))

     (context "Backstage Passes"
       (it "has a name"
           (should= (passes :name) "Backstage passes to a TAFKAL80ETC concert"))

       (it "has a sell-in value"
           (should-not-be-nil (passes :sell-in)))

       (it "has a quality"
           (should-not-be-nil (passes :quality))))

     (context "Aged Brie"
       (it "has a name"
           (should= (brie :name) "Aged Brie"))

       (it "has a sell-in value"
           (should-not-be-nil (brie :sell-in)))

       (it "has a quality"
           (should-not-be-nil (brie :quality)))))

  (context "#update-sell-in"
    (it "decreases the sell-in value"
      (let [updated-sell-in (update-sell-in default-item)]
        (should= (:sell-in updated-sell-in) 9))))

  (context "#update-quality"
    (it "decreases the quality"
      (should= (:quality updated-quality) 19))

    (it "does not allow quality to be negative"
      (let [min-quality (update-times default-item 100 update-quality)]
        (should= 0 (:quality min-quality))))

    (it "does not allow quality to be greater than 50"
      (let [max-quality (update-times brie 100 update-quality)]
        (should= 50 (:quality max-quality)))))

  (context "#update-item"
     (it "updates the quality and sell-in"
         (should= (:sell-in updated-item) 9)
         (should= (:quality updated-item) 19))

    (context "Sulfuras"
      (it "never decreases the quality or sell-in"
        (let [updated-sulfuras (update-item sulfuras)]
            (should= (:quality updated-sulfuras) 80)
            (should= (:sell-in updated-sulfuras) 0))))

    (context "Backstage Passes"
      (it "increases quality by 1 if sell-in is greater than 10"
        (let [fourteen-day (update-item passes)]
          (should= (:quality fourteen-day) (inc (:quality passes)))))

      (it "increases quality by 2 if sell-in is 10 or less"
        (let [nine-day (update-times passes 6 update-item) ten-day (update-times passes 5 update-item)]
          (should= (:quality ten-day) 25)
          (should= (:quality nine-day) 27)))

      (it "increases quality by 3 if sell-in is 5 or less"
        (let [four-day (update-times passes 11 update-item) five-day (update-times passes 10 update-item)]
          (should= (:quality five-day) 35)
          (should= (:quality four-day) 38)))

      (it "drops quality to 0 after sell-in day"
        (let [expired (update-times passes 16 update-item)]
          (should= (:quality expired) 0))))

    (context "Aged Brie"
      (it "increases in quality"
        (let [updated-brie (update-item brie)]
          (should= 1 (:quality updated-brie))))))

  (context "#update-inventory"
    (it "updates all items in inventory"
       (should-not= updated-inventory inventory))

    (it "updates the quality and sell-in for all items except Sulfuras"
      (let [updated-vest (find-by-name "+5 Dexterity Vest" updated-inventory)                   updated-elixir (find-by-name "Elixir of the Mongoose" updated-inventory)
            updated-brie (find-by-item :brie updated-inventory)
            updated-passes (find-by-item :passes updated-inventory)
            updated-sulfuras (find-by-item :sulfuras updated-inventory)]

        (should= (dec (:sell-in vest)) (:sell-in updated-vest))
        (should= (dec (:sell-in elixir)) (:sell-in updated-elixir))
        (should= (dec (:sell-in brie)) (:sell-in updated-brie))
        (should= (dec (:sell-in passes)) (:sell-in updated-passes))
        (should= (:sell-in sulfuras) (:sell-in updated-sulfuras))

        (should= (dec (:quality vest)) (:quality updated-vest))
        (should= (dec (:quality elixir)) (:quality updated-elixir))
        (should= (inc (:quality brie)) (:quality updated-brie))
        (should= (inc (:quality passes)) (:quality updated-passes))
        (should= (:quality sulfuras) (:quality updated-sulfuras))))))
