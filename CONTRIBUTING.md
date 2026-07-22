# Contributing

`cloud-itonami-iso3166-mrt` accepts contributions to the OSS actor, governor tests,
documentation, examples and open business blueprint.

## Development

```bash
clojure -M:dev:test
clojure -M:lint
```

Keep changes small and include tests for governor, audit or disclosure
behavior.

## Rules

- Do not commit real client, business-registration or bid data.
- Keep production actions behind the Market-Entry Compliance Governor.
- Never let the advisor state a legal/tax conclusion as fact — every
  regulatory requirement must cite the official source for the Islamic
  Republic of Mauritania. In particular, do not re-assume Mauritania
  shares another OHADA member state's company-law regime — this
  iteration independently confirmed Mauritania is NOT an OHADA member;
  verify before citing, don't copy a sibling catalog's framing.
- Document any new business-model or operator assumption in `docs/`.

## Pull Requests

PRs should describe:

- what behavior changed
- which governor invariant is affected
- how it was tested
- whether operator or certification docs need updates
