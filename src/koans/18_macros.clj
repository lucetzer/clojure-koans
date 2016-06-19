(ns koans.18-macros
  (:require [koan-engine.core :refer :all]))

(defmacro hello [x]
  (str "Hello, " x))

(defmacro infix [form]
  (list (second form) (first form) (nth form 2)))

(defmacro infix-better [form]
  `(~(second form) ; Note the syntax-quote (`) and unquote (~) characters!
    ~(first form)
    ~(nth form 2) ))

;(quote (+ 1 2 3)) => (+ 1 2 3) wrapping a quote around a form will return that form, without evaluating it.
; short hand for a quote is '(1 2 3)
; syntax-quote and unquote- tilde (~) as saying that we bounce outside the surrounding syntax-quoted form and evaluate the following form in that context,
; inserting the result back where the tilde was
; ~@ splicing unquote can take multiple forms to be inserted in the place of a single unquote
; unquote operations has to be inside a syntax-quote form e.g. you cannot have ~(str 1 2 3)
; more at http://blog.8thlight.com/colin-jones/2012/05/22/quoting-without-confusion.html

(defmacro r-infix [form]
  (cond (not (seq? form))
        form
        (= 1 (count form))
        `(r-infix ~(first form))
        :else
        (let [operator (second form)
              first-arg (first form)
              others ((comp rest rest) form)]
          `(~operator
            (r-infix ~first-arg)
            (r-infix ~others)))))

(meditations
  "Macros are like functions created at compile time"
  (= "Hello, Macros!" (hello "Macros!"))

  "I can haz infix?"
  (= 10 (infix (9 + 1)))
  ; (list (+ 9 1))

  "Remember, these are nothing but code transformations"
  (= '(+ 9 1) (macroexpand '(infix (9 + 1))))

  "You can do better than that - hand crafting FTW!"
  (= '(* 10 2) (macroexpand '(infix-better (10 * 2))))

  "Things don't always work as you would like them to... "
  (= '(+ 10 (2 * 3)) (macroexpand '(infix-better ( 10 + (2 * 3)))))

  "Really, you don't understand recursion until you understand recursion"
  (= 36 (r-infix (10 + (2 * 3) + (4 * 5)))))
