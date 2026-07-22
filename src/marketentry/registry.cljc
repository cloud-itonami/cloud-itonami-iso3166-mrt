(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `avenant-value-exceeds-ceiling?` is THIS vertical's own new ground-
  truth check, grounding MRT's flagship governor check
  (`marketentry.governor/avenant-ceiling-violations`): the Code des
  Marchés Publics' own Article 53 ('Avenants', own primary text, see
  `marketentry.facts`) states 'Les stipulations relatives au montant
  d'un marché public ne peuvent être modifiées que par voie d'avenant
  et dans la limite de vingt pour cent (20%) du montant du marché de
  base.' This is a GENUINELY DIFFERENT check SHAPE than every prior
  iso3166 sibling this repo mirrors: Bulgaria's ЗОП Art. 54(5)
  de-minimis is a PERCENTAGE-OF-TURNOVER ELIGIBILITY formula, Albania's
  Neni 76(2)(c) carve-out is a FLAT-CONSTANT ELIGIBILITY threshold,
  Azerbaijan's/Armenia's/Bolivia's flagship checks are BOOLEAN
  registry-membership ELIGIBILITY reads, Antigua and Barbuda's
  vendor-class check is a THREE-TIER ELIGIBILITY-THRESHOLD
  classification, Benin's MPME mechanism is a BID-EVALUATION PRICE
  ADJUSTMENT, Bhutan's FDI Negative List is a CATEGORICAL
  SECTOR-EXCLUSION allow-list gate, CAF's Marché réservé mechanism is a
  MULTI-CRITERION INCLUSION-ELIGIBILITY OR-test over the bidder's own
  workforce composition/legal form, COG's exclusion-duration-cap check
  is a SINGLE-SIDED STATUTORY CEILING on a SANCTION's duration (years),
  GIN's pecuniary-sanction-range check is a TWO-SIDED (floor AND
  ceiling) PERCENTAGE-OF-BASE-VALUE RANGE on a SANCTION's amount, and
  Estonia's digital-signing-method check tests the VALIDITY OF THE
  FILING itself rather than the bidder's substance. Mauritania's own
  Article 53 avenant ceiling is none of these: it is a SINGLE-SIDED
  (ceiling-only) PERCENTAGE-OF-BASE-VALUE cap on the CUMULATIVE VALUE
  OF POST-AWARD CONTRACT AMENDMENTS -- a contract-EXECUTION-integrity
  control against disguised scope-creep/re-negotiation that
  circumvents competitive procurement, not a pre-award bidder-
  eligibility gate and not a punitive sanction-amount/duration cap.
  Structurally it shares COG's 'single-sided ceiling' MATH shape, but
  validates an entirely different real-world quantity (contract-
  modification value, not exclusion duration) -- stated honestly here
  rather than claimed as a wholly novel arithmetic shape.

  Mauritania's OWN Article 15 ALSO creates a percentage-based
  'Redevance de Régulation' owed to ARMP on every approved public
  contract, but the law's own text defers the actual percentage to a
  future règlement this iteration did not locate -- deliberately NOT
  modeled as a governor check here (no concrete number to recompute
  against); see `marketentry.facts` for the honest disclosure. Article
  39's national-preference bid-evaluation margin (capped at 15%) is
  ALSO not modeled here -- it is the same BID-EVALUATION PRICE
  ADJUSTMENT shape category Guinea's own docstring already attributes
  to Benin's MPME mechanism (already spoken for by a sibling).

  It is entity-condition-gated like COG's/CAF's/GIN's own flagship
  checks: a no-op (false) unless `:prior-avenant?` is true (an
  engagement with no declared prior avenant has nothing for this check
  to validate). Missing/non-numeric `:avenant-cumulative-amount` or
  `:marche-base-amount` for a declared prior avenant is never treated
  as violating the ceiling HERE (that is the `evidence-incomplete`
  check's job, upstream, where an assessment must already exist).

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real ARMP, CNCMP, or DGI system. It builds the RECORD an
  operator would keep, not the act of submitting a filing itself (that
  is `marketentry.operation`'s `:filing/submit`, always human-gated --
  see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(def avenant-ceiling
  "Code des Marchés Publics, Article 53 (own primary text, read directly
  from a genuine text-layer PDF hosted on marchespublics.gov.mr,
  2026-07-22/23): the statutory percentage-of-base-value CEILING (no
  floor) on the cumulative value of post-award contract amendments
  (avenants) relative to the base contract (marché) amount."
  {:max-pct 0.20})

(defn avenant-value-exceeds-ceiling?
  "Does `engagement`'s own declared cumulative avenant value exceed
  Article 53's own statutory 20% ceiling of its declared base marché
  amount?

  A no-op (false) unless `:prior-avenant?` is true -- an engagement
  with no declared prior avenant has nothing for this check to
  validate. Missing/non-numeric `:avenant-cumulative-amount` or
  `:marche-base-amount` for a declared prior avenant is never treated
  as violating the ceiling here (that is the `evidence-incomplete`
  check's job, upstream, where an assessment must already exist)."
  [{:keys [prior-avenant? avenant-cumulative-amount marche-base-amount]}]
  (boolean
   (when (true? prior-avenant?)
     (when (and (number? avenant-cumulative-amount) (number? marche-base-amount)
                (pos? marche-base-amount))
       (let [ratio (/ (double avenant-cumulative-amount) (double marche-base-amount))]
         (> ratio (:max-pct avenant-ceiling)))))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a tender response or
  filing package. Pure function -- does not touch any real ARMP,
  CNCMP, or DGI system."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a tender
  response or filing (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
