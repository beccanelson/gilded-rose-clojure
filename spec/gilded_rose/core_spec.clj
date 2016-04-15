(ns gilded-rose.core-spec
 (:require [speclj.core :refer :all]
           [gilded-rose.core :refer :all]))

(def default-item (item "Default Item" 10 20))
(def sulfuras (make-specialty-item :sulfuras "Sulfuras, Hand of Ragnaros" 0 80))
(def passes (make-specialty-item :passes "Backstage passes to a TAFKAL80ETC concert" 15 20))
(def brie (make-specialty-item :brie "Aged Brie" 2 0))
(def elixir (item "Elixir of the Mongoose" 5 7))
(def vest (item "+5 Dexterity Vest" 10 20))
(def conjured (make-specialty-item :conjured "Conjured" 10 10))

(def updated-item (update-item default-item))

(def inventory [vest elixir brie sulfuras passes conjured])
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
      (let [updated-quality (update-quality default-item)]
        (should= 19 (:quality updated-quality))))

    (it "does not allow quality to be negative"
      (let [min-quality (update-times update-quality default-item 100)]
        (should= 0 (:quality min-quality))))

    (it "does not allow quality to be greater than 50"
      (let [max-quality (update-times update-quality brie 100)]
        (should= 50 (:quality max-quality))))

    (it "decreases quality by 2 if sell-in is less than 0"
      (let [sell-in-0 (update-times update-item default-item 10) sell-in-past (update-times update-item default-item 11)]
        (should= (- (:quality sell-in-0) 2) (:quality sell-in-past)))))

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
        (let [nine-day (update-times update-item passes 6) ten-day (update-times update-item passes 5)]
          (should= (:quality ten-day) 25)
          (should= (:quality nine-day) 27)))

      (it "increases quality by 3 if sell-in is 5 or less"
        (let [four-day (update-times update-item passes 11) five-day (update-times update-item passes 10)]
          (should= (:quality five-day) 35)
          (should= (:quality four-day) 38)))

      (it "drops quality to 0 after sell-in day"
        (let [expired (update-times update-item passes 16)]
          (should= (:quality expired) 0))))

    (context "Aged Brie"
      (it "increases in quality"
        (let [updated-brie (update-item brie)]
          (should= 1 (:quality updated-brie)))))

    (context "Conjured"
      (it "decreases in quality twice as fast"
        (let [updated-conjured (update-item conjured)]
          (should= (- (:quality conjured) 2) (:quality updated-conjured))))))

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
