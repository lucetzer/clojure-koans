(ns koans.17-atoms
  (:require [koan-engine.core :refer :all]))

; Atoms provide a way to manage shared, synchronous, independent state. They are a reference type like refs and vars.

(def atomic-clock (atom 0))

(meditations
  "Atoms are like refs"
  (= 0 @atomic-clock)

  "You can change at the swap meet"
  (= 1 (do
          (swap! atomic-clock inc)
          @atomic-clock))

  "Keep taxes out of this: swapping requires no transaction"
  (= 5 (do
         (swap! atomic-clock + 4)
         @atomic-clock))

  "Any number of arguments might happen during a swap"
  (= 20 (do
          (swap! atomic-clock + 1 2 3 4 5)
          @atomic-clock))

  "Atomic atoms are atomic"
  (= 20 (do
          (compare-and-set! atomic-clock 100 :fin)
          @atomic-clock))

  "When your expectations are aligned with reality, things proceed that way"
  (= :fin (do
            (compare-and-set! atomic-clock 20 :fin)
            @atomic-clock)))

;(compare-and-set! atom oldval newval)
;Atomically sets the value of atom to newval if and only if the current value of the atom is identical to oldval.
; Returns true if set happened, else false