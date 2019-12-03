(ns advent-of-code-2019.day2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn intcode
  [prog idx]
  (let [[op src1 src2 dest] (drop idx prog)
        calc #(intcode (assoc prog dest (% (get prog src1) (get prog src2))) (+ 4 idx))]
    (condp = op
      1 (calc +)
      2 (calc *)
      99 prog
      (throw (Exception. (str "Unknown operand."))))))

(defn run
  [noun verb]
  (as-> (slurp (io/resource "day2.txt")) x
    (str/trim-newline x)
    (str/split x #",")
    (mapv #(Integer/parseInt %) x)
    (assoc x 1 noun)
    (assoc x 2 verb)
    (intcode x 0)))

#_(intcode [1,0,0,0,99] 0)
#_(intcode [2,3,0,3,99] 0)
#_(intcode [2,4,4,5,99,0] 0)
#_(intcode [1,1,1,4,99,5,6,0,99] 0)

(defn search
  [t]
  (->> (for [noun (range) verb (range 100)
             :let [[r] (run noun verb)]
             :when (= r t)]
         (+ (* 100 noun) verb))
       (first)))
