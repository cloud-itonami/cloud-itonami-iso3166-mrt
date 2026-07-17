(ns culture.facts
  "Country-level regional-culture catalog for Mauritania (MRT) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"MRT"
   [{:culture/id "mrt.dish.thieboudienne"
     :culture/name "Thieboudienne"
     :culture/country "MRT"
     :culture/kind :dish
     :culture/summary "Coastal dish of fish and rice served in a white and red tomato sauce, considered the national dish of Mauritania per the Mauritanian cuisine article (a Senegalese-origin dish also consumed across West Africa)."
     :culture/url "https://en.wikipedia.org/wiki/Mauritanian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mrt.dish.mechoui"
     :culture/name "Méchoui"
     :culture/country "MRT"
     :culture/kind :dish
     :culture/summary "Whole roasted lamb dish listed among Mauritanian cuisine's main dishes."
     :culture/url "https://en.wikipedia.org/wiki/Mauritanian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mrt.dish.cherchem"
     :culture/name "Cherchem"
     :culture/country "MRT"
     :culture/kind :dish
     :culture/summary "Mauritanian lamb couscous dish listed among Mauritanian cuisine's main dishes."
     :culture/url "https://en.wikipedia.org/wiki/Mauritanian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mrt.beverage.zrig"
     :culture/name "Zrig"
     :culture/country "MRT"
     :culture/kind :beverage
     :culture/summary "Fermented milk beverage of the west Sahara, a traditional Mauritanian drink of soured milk mixed with sugar and water."
     :culture/url "https://en.wikipedia.org/wiki/Zrig"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mrt.beverage.mint-tea"
     :culture/name "Mauritanian mint tea"
     :culture/country "MRT"
     :culture/kind :beverage
     :culture/summary "Traditional tea preparation poured from height to create foam, listed among Mauritanian cuisine's beverages."
     :culture/url "https://en.wikipedia.org/wiki/Mauritanian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mrt.craft.chinguetti-manuscripts"
     :culture/name "Chinguetti manuscript libraries"
     :culture/country "MRT"
     :culture/kind :craft
     :culture/summary "The old quarter of Chinguetti, Mauritania holds five manuscript libraries of scientific and Qur'anic texts dating from the later Middle Ages, reflecting the town's historic role as a center of Islamic scholarship in West Africa."
     :culture/url "https://en.wikipedia.org/wiki/Chinguetti"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mrt.heritage.ancient-ksour"
     :culture/name "Ancient Ksour of Ouadane, Chinguetti, Tichitt and Oualata"
     :culture/country "MRT"
     :culture/kind :heritage
     :culture/summary "Four ancient caravan cities in Mauritania founded around the 11th-12th centuries as trans-Saharan trade hubs, inscribed on the UNESCO World Heritage List in 1996."
     :culture/url "https://en.wikipedia.org/wiki/Ancient_Ksour_of_Ouadane,_Chinguetti,_Tichitt_and_Oualata"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mrt culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "MRT"))
                 " MRT entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
