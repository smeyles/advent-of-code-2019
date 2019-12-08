(ns advent-of-code-2019.day3
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn load-data
  [file]
  (->> (str/split-lines (slurp (io/resource file)))
       (map #(str/split % #","))))

(defn add-path
  [d l pts]
  (loop [l l
         pts pts]
    (if (zero? l)
      pts
      (recur
        (dec l)
        (cons (mapv #(+ %1 %2) d (or (first pts) [0 0])) pts)))))

(def direction->offsets
  {"R" [1 0] "L" [-1 0] "U" [0 -1] "D" [0 1]})

(defn paths->points
  [paths]
  (loop [paths paths
         points nil]
    (if-let [[_ d l] (re-matches #"([RLUD])(\d*)" (or (first paths) ""))]
      (recur
        (rest paths)
        (add-path (get direction->offsets d) (Integer/parseInt l) points))
      points)))

(defn manhattan-distance
  [_ _ [x y]]
  (+ (Math/abs x) (Math/abs y)))

(defn distance-to-point
  [path point]
  (inc (count (take-while #(not= % point) path))))

(defn min-sum-path-length
  [path1 path2 point]
  (+ (distance-to-point (reverse path1) point)
     (distance-to-point (reverse path2) point)))

(defn minimize
  [min-fn [p1 p2]]
  (let [m (partial min-fn p1 p2)
        intersections (set/intersection (set p1) (set p2))
        min-pair (apply min-key m intersections)]
    (m min-pair)))

(defn run
  [part2?]
  (->> (load-data "day3.txt")
       (map paths->points)
       ((partial minimize (if part2? min-sum-path-length manhattan-distance)))))
