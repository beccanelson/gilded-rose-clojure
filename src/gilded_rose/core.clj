(ns gilded-rose.core)

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn make-specialty-item [type name sell-in quality]
  {:item type :name name :sell-in sell-in :quality quality})

(defn call-function-times [function-to-apply item times]
  (loop [times times current-item item]
    (if (= times 0)
      current-item
      (recur (dec times) (function-to-apply current-item)))))

(defmulti update-sell-in :item)

(defmulti update-quality :item)

(defn update-item-attribute [item attribute function]
  (merge item {attribute function}))

(defn increase-quality [item]
  (if (< (:quality item) 50)
    (update-item-attribute item :quality (inc (:quality item)))
    item))

(defn decrease-quality [item]
  (if (> (:quality item) 0)
    (update-item-attribute item :quality (dec (:quality item)))
    item))

(defmethod update-sell-in :default [item]
  (update-item-attribute item :sell-in (dec (:sell-in item))))

(defn number-of-times-to-update-quality [item]
  (if (< (:sell-in item) 0)
  2
  1))

(defmethod update-quality :default [item]
  (let [times-to-update (number-of-times-to-update-quality item)]
    (call-function-times decrease-quality item times-to-update)))

(defmethod update-quality :passes [item]
  (increase-quality item))

(defmethod update-quality :brie [item]
  (increase-quality item))

(defmethod update-quality :conjured [item]
  (let [times-to-update (* 2 (number-of-times-to-update-quality item))]
    (call-function-times decrease-quality item times-to-update)))

(defmulti update-quality-and-sell-in :item)

(defmethod update-quality-and-sell-in :default [item]
  (update-quality (update-sell-in item)))

(defmethod update-quality-and-sell-in :sulfuras [sulfuras]
  sulfuras)

(defmethod update-quality-and-sell-in :passes [passes]
  (let [updated-passes (update-sell-in passes)]
    (cond
      (< (:sell-in updated-passes) 0)
        (update-item-attribute passes :quality 0)
      (< (:sell-in updated-passes) 5)
        (call-function-times update-quality updated-passes 3)
      (< (:sell-in updated-passes) 10)
        (call-function-times update-quality updated-passes 2)
      :else
        (update-quality updated-passes))))

(defn update-inventory [items]
  (map update-quality-and-sell-in items))
