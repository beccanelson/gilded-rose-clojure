(ns gilded-rose.core-spec
 (:require [speclj.core :refer :all]
           [gilded-rose.core :refer :all]))

(def sulfuras (make-sulfuras 0 80))
(def passes (make-passes 15 20))
(def brie (make-brie 2 0))
(def default-item (item "Default Item" 10 20))
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
      (should= (:quality updated-quality) 19))

    (it "does not allow quality to be negative"
      (let [min-quality (update-times default-item 100 update-item-quality)]
        (should= 0 (:quality min-quality))))

    (it "does not allow quality to be greater than 50"
      (let [max-quality (update-times brie 100 update-item-quality)]
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
          (should= (:quality expired) 0)))))

  (context "Aged Brie"
    (it "increases in quality"
      (let [updated-brie (update-item brie)]
        (should= 1 (:quality updated-brie))))))
