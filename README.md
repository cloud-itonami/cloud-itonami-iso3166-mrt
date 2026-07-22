# cloud-itonami-iso3166-mrt

**MRT**: Islamic Republic of Mauritania.

- ARMP (Autorité de Régulation des Marchés Publics) public procurement,
  rattachée à la Présidence de la République; CNCMP (Commission
  Nationale de Contrôle des Marchés Publics), placée sous la tutelle du
  Premier Ministre, a priori/a posteriori control; Code des Marchés
  Publics (Loi n° 2021-024, abrogeant la Loi n° 2010-044 du 22 juillet
  2010, Journal Officiel n°1501 du 15 janvier 2022)
- **Mauritania is NOT an OHADA member state** (independently confirmed
  this iteration, overturning the working assumption that it shares
  CAF/COG/GIN's OHADA-derived company-law regime -- see
  `src/marketentry/facts.cljc`'s namespace docstring). No RCCM/AUSCGIE
  citation is asserted here; business/company-registration and
  investment-code citations are an honestly-disclosed gap this
  iteration could not independently confirm.
- NIF (DGI, Direction Générale des Impôts) tax registration
- Code des Marchés Publics Article 53 avenant (contract-amendment)
  value ceiling, 20% of base marché amount (flagship check)

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as `cloud-itonami-iso3166-caf`/`-cog`/`-gin`:

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Code des
  Marchés Publics (Loi n° 2021-024, read in FULL from a genuine
  text-layer PDF hosted on `marchespublics.gov.mr`, no OCR needed)
  creating ARMP (Article 12, attached to the Presidency) and CNCMP
  (Article 10, under the Prime Minister) as the separate control body.
  **This iteration independently verified Mauritania is NOT an OHADA
  member state** (OHADA's own member-states page AND French
  Wikipedia's OHADA article, both fetched directly, neither mentions
  Mauritania) -- so, unlike the CAF/COG/GIN siblings, no RCCM/AUSCGIE
  citation is used here; business/company-registration is an honestly-
  disclosed gap (see docstring for the full list of official sites
  checked and found empty). `governor.cljc`'s flagship check
  independently recomputes whether an engagement's own declared
  cumulative avenant (contract-amendment) value exceeds the Code's own
  Article 53 statutory 20% ceiling of its base marché amount -- a
  SINGLE-SIDED percentage-of-base-value ceiling on POST-AWARD contract-
  modification value, a check domain genuinely different from every
  other iso3166 sibling's flagship (see the namespace docstrings for
  the full research trail and honestly-narrowed scope).
- `src/statute/facts.cljc` -- general-law catalog: deliberately smaller
  than the CAF/COG/GIN siblings' own (no AUSCGIE entry, since
  Mauritania is not OHADA) -- just the Code du Travail (Loi n° 2004-017,
  confirmed via the Ministère de la Fonction Publique et du Travail's
  own site).

Every citation is curl/WebFetch-verified against an official source
(`armp.mr`, `marchespublics.gov.mr`, `impots.gov.mr/DGI`,
`finances.gov.mr`, `fonctionpublique.gov.mr`, `ohada.org`); the Code
des Marchés Publics PDF is a genuine text-layer (born-digital) document,
no OCR needed. `WebSearch` was unavailable this iteration (session
budget already exhausted) -- every fact rests on direct WebFetch/curl
fetches of specific URLs, not a search engine, and several official
Mauritanian ministry sites were checked and found NOT to carry the
content this iteration was looking for (business registration /
Guichet Unique / Code des Investissements) -- an honestly-disclosed gap,
not papered over. `droit-afrique.com`, this fleet's usual secondary
fallback, was entirely unreachable this session (HTTP 403 site-wide).
See `marketentry.facts`'s docstring for exactly which facts are HIGH
confidence vs. an honestly-flagged gap.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Mauritania:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
