(ns statute.facts
  "General-law compliance catalog for the Islamic Republic of Mauritania
  (MRT) -- extends this repo's existing `marketentry.facts` (public-
  procurement market-entry only, narrow scope) with a second, orthogonal
  catalog of statutes a company operating in this jurisdiction must
  generally track for compliance. Mirrors cloud-itonami-iso3166-ben/
  -btn/-caf/-cog/-gin's `statute.facts` (ADR-2607141700, cloud-itonami-
  compliance-fact-federation).

  Every entry below cites an OFFICIAL government-hosted URL -- never
  fabricated, curl/WebFetch-verified 2026-07-22/23:

  - **This catalog is deliberately SMALLER than the CAF/COG/GIN
    siblings' own statute.facts (which each carry an OHADA AUSCGIE
    company-law entry).** This iteration independently confirmed
    Mauritania is NOT an OHADA member state (see `marketentry.facts`
    namespace docstring for the full two-source confirmation: OHADA's
    own member-states page AND French Wikipedia's OHADA article, both
    fetched directly, neither mentions Mauritania in any capacity). So
    there is no AUSCGIE entry to honestly cite here, and this iteration
    did NOT find Mauritania's own (necessarily national, not
    supranational) companies-law/Code de Commerce citation despite
    checking the Ministry of Commerce and Tourism
    (`commerce.gov.mr`), the Ministry of Finance (`finances.gov.mr`),
    the Ministry of Economic Affairs and Development
    (`economie.gov.mr`), UNCTAD's Investment Policy Hub (JS-rendered,
    not retrievable via static fetch), and Mauritania's own (dead)
    eRegulations mirror (`mauritanie.eregulations.org` -> 'no available
    server'). `droit-afrique.com`, this fleet's usual secondary
    fallback, was entirely unreachable this session (HTTP 403 on every
    path tried, including its bare homepage, via both curl and
    WebFetch). A smaller, 100%-honest catalog beats a fabricated
    comprehensive one -- see `coverage`'s note.
  - **Code du Travail (Labour Code)**: Loi n° 2004-017 portant Code du
    Travail -- this iteration confirmed this directly from the
    Ministère de la Fonction Publique et du Travail's own site
    (`fonctionpublique.gov.mr/fr/node/1036`, WebFetch-verified
    directly), which hosts a downloadable PDF titled verbatim 'Loi N°
    2004-017 portant code du travail.pdf' under its own 'Textes
    Juridiques' section. This iteration did NOT download and read the
    PDF's own full body text (only the ministry's own citing page),
    an honest gap narrower than this catalog's Code du Travail
    entries for other jurisdictions where the full primary text was
    read (e.g. Guinea's own Code du Travail entry, OCR-read in full).
  - **Public procurement** (Code des Marchés Publics, Loi n° 2021-024)
    is the PRIMARY subject of `marketentry.facts` in this repo and is
    not duplicated here to avoid a redundant second citation of the
    same law under a different catalog.
  - This iteration did NOT confirm a Code des Investissements
    (Investment Code) citation for Mauritania this session -- an
    honest gap, not resolved by guessing a law number (see
    `marketentry.facts` namespace docstring for the full list of
    official sites checked).

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"MRT"
   [{:statute/id "mrt.code-du-travail-2004"
     :statute/title "Code du Travail (Islamic Republic of Mauritania)"
     :statute/jurisdiction "MRT"
     :statute/kind :law
     :statute/law-number "Loi n° 2004-017 (own citing page, Ministère de la Fonction Publique et du Travail's own site; this iteration did not independently fetch the law's own full primary-text PDF body, only the ministry's own citing page and download-link title, an honest gap)"
     :statute/url "https://www.fonctionpublique.gov.mr/fr/node/1036"
     :statute/url-provenance :official-ministere-fonction-publique-travail
     :statute/enacted-date "2004"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor :employment}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mrt statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "MRT")) " MRT statute(s) seeded with an "
                 "official citation. Deliberately smaller than the CAF/COG/GIN "
                 "siblings' catalogs -- Mauritania is NOT an OHADA member state "
                 "(independently confirmed, see namespace docstring), so no "
                 "AUSCGIE entry applies, and no Mauritania-specific company-law "
                 "citation was independently confirmed this iteration. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :corporate-governance)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
