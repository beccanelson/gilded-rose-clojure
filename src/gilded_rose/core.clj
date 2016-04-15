(ns gilded-rose.core)

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn item-name [item] (:name item))

(defn make-sulfuras [name sell-in quality]
  {:item :sulfuras :name name :sell-in sell-in :quality quality})

(defn make-passes [name sell-in quality]
  {:item :passes :name name :sell-in sell-in :quality quality})

(defn make-brie [name sell-in quality]
  {:item :brie :name name :sell-in sell-in :quality quality})

(defn make-conjured [name sell-in quality]
  {:item :conjured :name name :sell-in sell-in :quality quality})

(defn update-times [function item times]
  (loop [times times current-item item]
    (if (= times 0)
      current-item
      (recur (dec times) (function current-item)))))

(defmulti update-sell-in :item)

(defmulti update-quality :item)

(defn update [item attribute function]
  (merge item {attribute function}))

(defn increase-quality [item]
  (if (< (:quality item) 50)
    (update item :quality (inc (:quality item)))
    item))

(defn decrease-quality [item]
  (if (> (:quality item) 0)
    (update item :quality (dec (:quality item)))
    item))

(defmethod update-sell-in :default [item]
  (update item :sell-in (dec (:sell-in item))))

(defmethod update-quality :default [item]
  (if (< (:sell-in item) 0)
    (update-times decrease-quality item 2)
    (decrease-quality item)))

(defmethod update-quality :passes [item]
  (increase-quality item))

(defmethod update-quality :brie [item]
  (increase-quality item))

(defmethod update-quality :conjured [item]
  (if (< (:sell-in item) 0)
    (update-times decrease-quality item 4)
    (update-times decrease-quality item 2)))

(defmulti update-item :item)

(defmethod update-item :default [item]
  (let [updated-item (update-sell-in item)]
    (update-quality updated-item)))

(defmethod update-item :sulfuras [sulfuras]
  sulfuras)

(defmethod update-item :passes [passes]
  (let [updated-passes (update-sell-in passes)]
    (cond
      (< (:sell-in updated-passes) 0)
      (update passes :quality 0)
      (< (:sell-in updated-passes) 5)
      (update-times update-quality updated-passes 3)
      (< (:sell-in updated-passes) 10)
      (update-times update-quality updated-passes 2)
      :else
        (update-quality updated-passes))))

(defn update-inventory [items]
  (map update-item items))

(defn update-current-inventory[]
  (let [inventory
        [
         (item "+5 Dexterity Vest" 10 20)
         (make-brie "Aged Brie" 2 0)
         (item "Elixir of the Mongoose" 5 7)
         (make-sulfuras "Sulfuras, Hand of Ragnaros" 0 80)
         (make-passes "Backstage passes to a TAFKAL80ETC concert" 15 20)]]

    (update-inventory inventory)))
