# Carepoint Hospital Manager — project notes

## Goal
Explore a clear dashboard for appointment flow and care-team operations. Carepoint is a fictional portfolio concept. Names, dates, counts, and appointment details shown in the preview are illustrative only; no real patient data is used.

## What the demo contains
The preview in the root `index.html` is a static HTML/CSS dashboard with summary tiles and a short appointment list. It has no database, patient-record system, login, role enforcement, scheduling engine, or healthcare-service connection. The `Java · SQL concepts` label describes a possible learning direction; this repository contains no Java backend.

## Code guide
- `.mockhead` shows the dashboard identity and date context.
- `.cards` and `.tile` display fictional operational summaries.
- `.rows` and `.row` lay out example appointment entries and status context.
- CSS Grid aligns labels and supporting text; the narrow-screen media query changes the showcase to one column.
- Root-page JavaScript filters project cards only. It does not store or change dashboard data.

## Responsible next steps
A real healthcare product would need privacy and security review, secure authentication and authorization, audit trails, data minimization, accessibility testing, and careful validation with qualified users. This visual concept is not a clinical system.

## References
- [NHS Service Manual](https://service-manual.nhs.uk/) — healthcare-service design context.
- [GOV.UK Design System](https://design-system.service.gov.uk/) — clear content and accessible design conventions.
- [W3C WAI Tables Tutorial](https://www.w3.org/WAI/tutorials/tables/) — guidance if the appointment list later becomes a data table.

Carepoint is fictional and not affiliated with NHS or GOV.UK.
