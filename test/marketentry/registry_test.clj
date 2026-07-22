(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "MRT" 0)
        s (registry/register-submit "eng-1" "MRT" 0)]
    (is (= "MRT-DFT-000000" (get d "draft_number")))
    (is (= "MRT-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "MRT" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest avenant-ceiling-recompute
  (testing "Code des Marchés Publics Art.53 -- a declared avenant within the 20% statutory ceiling is fine"
    (is (false? (registry/avenant-value-exceeds-ceiling?
                 {:prior-avenant? true
                  :avenant-cumulative-amount 150000 :marche-base-amount 1000000}))))
  (testing "a declared avenant ABOVE the 20% ceiling is a violation"
    (is (true? (registry/avenant-value-exceeds-ceiling?
                {:prior-avenant? true
                 :avenant-cumulative-amount 250000 :marche-base-amount 1000000}))))
  (testing "exactly the ceiling (20%) does not violate it"
    (is (false? (registry/avenant-value-exceeds-ceiling?
                 {:prior-avenant? true
                  :avenant-cumulative-amount 200000 :marche-base-amount 1000000}))))
  (testing "entity-condition-gated: a no-op (false) unless :prior-avenant? is true"
    (is (false? (registry/avenant-value-exceeds-ceiling?
                 {:prior-avenant? false
                  :avenant-cumulative-amount 999999 :marche-base-amount 1000000})))
    (is (false? (registry/avenant-value-exceeds-ceiling? {}))))
  (testing "missing/non-numeric amounts for a declared prior avenant are never treated as violating the ceiling here (evidence-incomplete's job)"
    (is (false? (registry/avenant-value-exceeds-ceiling?
                 {:prior-avenant? true})))
    (is (false? (registry/avenant-value-exceeds-ceiling?
                 {:prior-avenant? true :avenant-cumulative-amount nil
                  :marche-base-amount 1000000})))
    (is (false? (registry/avenant-value-exceeds-ceiling?
                 {:prior-avenant? true :avenant-cumulative-amount 150000
                  :marche-base-amount 0})))))
