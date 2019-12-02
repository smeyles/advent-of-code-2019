(ns advent-of-code-2019.day1
  (:require [clojure.java.io :as io]))

(defn mass-to-fuel
  [m]
  (max 0 (- (quot m 3) 2)))

(defn reduce-to-fuel
  [m]
  (->> (iterate mass-to-fuel m)
       (drop 1)
       (take-while pos?)
       (apply +)))

(defn run
  [part2?]
  (->> (io/reader (io/resource "day1.txt"))
       line-seq
       (map #(Integer/parseInt %))
       (map (if part2? reduce-to-fuel mass-to-fuel))
       (apply +)))
