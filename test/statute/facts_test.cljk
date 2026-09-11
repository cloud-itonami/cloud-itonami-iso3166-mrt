(ns statute.facts-test
  (:require [kotoba.lang.text :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest mrt-has-spec-basis
  (let [sb (facts/spec-basis "MRT")]
    (is (= 1 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["MRT" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["mrt.code-du-travail-2004"]
         (mapv :statute/id (facts/by-topic "MRT" :labor))))
  (is (empty? (facts/by-topic "MRT" :corporate-governance)))
  (is (empty? (facts/by-topic "MRT" :environment)))
  (is (empty? (facts/by-topic "ATL" :labor))))
