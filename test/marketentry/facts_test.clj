(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest mrt-has-spec-basis
  (let [sb (facts/spec-basis "MRT")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "MRT")))
    (is (some? (facts/avenant-ceiling-spec-basis "MRT")))))

(deftest mrt-rep-spec-basis-is-populated
  (testing "Code des Marchés Publics Art. 25(f)'s exclusion-extension provision covers entities directed by, or majority-capital-held by, a previously-excluded person"
    (let [rep (facts/rep-spec-basis "MRT")]
      (is (some? rep))
      (is (string? (:rep-owner-authority rep)))
      (is (string? (:rep-legal-basis rep))))))

(deftest mrt-business-registration-is-honestly-nil
  (testing "Mauritania is NOT an OHADA member (independently confirmed) -- no RCCM/AUSCGIE citation is asserted, and no Mauritania-specific company-registration mechanism was confirmed this iteration"
    (is (nil? (facts/business-registration-spec-basis "MRT")))))

(deftest mrt-avenant-ceiling-is-the-flagship-spec-basis
  (testing "Code des Marchés Publics Art. 53's 20% avenant ceiling is a real, verifiable statutory ceiling -- not fabricated"
    (let [ac (facts/avenant-ceiling-spec-basis "MRT")]
      (is (some? ac))
      (is (= 0.20 (:avenant-ceiling-max-pct ac)))
      (is (string? (:avenant-ceiling-legal-basis ac))))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ")))
  (is (nil? (facts/business-registration-spec-basis "ATL")))
  (is (nil? (facts/avenant-ceiling-spec-basis "ATL")))
  (is (nil? (facts/rep-spec-basis "ATL"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "MRT")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "MRT" all)))
    (is (not (facts/required-evidence-satisfied? "MRT" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["MRT" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
