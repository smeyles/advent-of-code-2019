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

(def r1 "R8,U5,L5,D3")
(def r2 "U7,R6,D4,L4")

(def s1 "R75,D30,R83,U83,L12,D49,R71,U7,L72")
(def s2 "U62,R66,U55,R34,D71,R55,D58,R83")

(defn run
  [part2?]
  (->> (load-data "day3.txt")
       #_(map #(str/split % #",") [r1 r2])
       (map paths->points)
       ((partial minimize (if part2? min-sum-path-length manhattan-distance)))))
