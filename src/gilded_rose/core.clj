(ns gilded-rose.core)

; item has a name, sell-in, and quality
(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-times [item times function]
  (loop [times times current-item item]
    (if (= times 0)
      current-item
      (recur (dec times) (function current-item)))))

(defmulti update-sell-in :item)

(defmulti update-item-quality :item)

(defn update [item attribute function]
  (merge item {attribute function}))

(defmethod update-sell-in :default [item]
  (update item :sell-in (dec (:sell-in item))))

(defmethod update-item-quality :default [item]
  (if (> (:quality item) 0)
    (update item :quality (dec (:quality item)))
    item))

(defmethod update-item-quality (or :passes :brie) [item]
  (if (< (:quality item) 50)
    (update item :quality (inc (:quality item)))))

(defn make-sulfuras [sell-in, quality]
  {:item :sulfuras :name "Sulfuras, Hand of Ragnaros" :sell-in sell-in :quality quality})

(defn make-passes [sell-in quality]
  {:item :passes :name "Backstage passes to a TAFKAL80ETC concert" :sell-in sell-in :quality quality})

(defn make-brie [sell-in quality]
  {:item :brie :name "Aged Brie" :sell-in sell-in :quality quality})

(defmulti update-item :item)

(defmethod update-item :default [item]
  (let [updated-item (update-sell-in item)]
    (update-item-quality updated-item)))

(defmethod update-item :sulfuras [sulfuras]
  sulfuras)

(defmethod update-item :passes [passes]
  (let [updated-passes (update-sell-in passes)]
    (cond
      (< (:sell-in updated-passes) 0)
      (update passes :quality 0)
      (< (:sell-in updated-passes) 5)
      (update-times updated-passes 3 update-item-quality)
      (< (:sell-in updated-passes) 10)
      (update-times updated-passes 2 update-item-quality)
      :else
        (update-item-quality updated-passes))))


(defn update-inventory [items]
  (map update-item items))


(defn update-quality [items]
  (map
    (fn[item] (cond
      ; if sell-in is less than 0 and item is backstage passes
               (and (< (:sell-in item) 0) (= "Backstage passes to a TAFKAL80ETC concert" (:name item)))
        ; quality = 0
               (merge item {:quality 0})
      ; if item is aged brie or backstage passes
               (or (= (:name item) "Aged Brie") (= (:name item) "Backstage passes to a TAFKAL80ETC concert"))
        ; if item is backstage passes and sell-in is between 5 and 9
               (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 5) (< (:sell-in item) 10))
          ; quality increases by 2
                 (merge item {:quality (inc (inc (:quality item)))})
          ; else if item is backstage passes and sell-in is between 0 and 4
                 (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 0) (< (:sell-in item) 5))
            ; quality increases by 3
                   (merge item {:quality (inc (inc (inc (:quality item))))})
            ; else if item quality is less than 50
                   (if (< (:quality item) 50)
              ; quality increases by 1
                     (merge item {:quality (inc (:quality item))})
                     item)))
      ; if item sell-in is less than 0
               (< (:sell-in item) 0)
        ; if item is backstage passes
               (if (= "Backstage passes to a TAFKAL80ETC concert" (:name item))
          ; qualty = 0
                 (merge item {:quality 0})
          ; if item is dexterity vest or elixir of the mongoose
                 (if (or (= "+5 Dexterity Vest" (:name item)) (= "Elixir of the Mongoose" (:name item)))
            ; quality decreases by 2
                   (merge item {:quality (- (:quality item) 2)})
                   item))
      ; if item is exterity vest or elixr of the mongoose
               (or (= "+5 Dexterity Vest" (:name item)) (= "Elixir of the Mongoose" (:name item)))
        ;quality decreases by 1
               (merge item {:quality (dec (:quality item))})
      ; for all other items
               :else item))
   (map (fn [item]
      ; if item is not sulfuras
         (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
        ; decrease sell-in
           (merge item {:sell-in (dec (:sell-in item))})
           item))
    items)))


; starting values for inventory
(defn update-current-inventory[]
  (let [inventory
        [
          (item "+5 Dexterity Vest" 10 20)
          (item "Aged Brie" 2 0)
          (item "Elixir of the Mongoose" 5 7)
          (make-sulfuras 0 80)
          (item "Backstage passes to a TAFKAL80ETC concert" 15 20)]]

    (update-quality inventory)))
