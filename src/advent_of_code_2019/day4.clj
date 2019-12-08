(ns advent-of-code-2019.day4)

(defn increasing?
  ([n] (increasing? n 10))
  ([n min]
   (if (< n 10)
     (<= n min)
     (let [min' (mod n 10)]
       (and (<= min' min) (increasing? (quot n 10) min'))))))

(defn adjacent?
  ([n]
   (loop [n n prior -1]
     (let [current (mod n 10)]
       (if (= prior current)
         true
         (if (< n 10)
           false
           (recur (quot n 10) current)))))))

(defn has-pair?
  [n]
  (some #(= 2 %) (vals (frequencies (str n)))))

(defn validate
  [n f]
  (and (increasing? n) (adjacent? n) (f n)))

(defn run
  [low high part2?]
  (loop [n low count 0]
    (if (<= n high)
      (recur (inc n) (if (validate n (if part2? has-pair? (constantly true)))
                       (inc count) count))
      count)))
