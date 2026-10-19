# Studywise AI Learning Companion — project notes

## Goal
Explore a student-led study planner that breaks a learning goal into smaller activities and encourages reflection. The current screen is a static product concept; it does not call an AI model or generate responses dynamically.

## What the demo contains
The root `index.html` includes a weekly progress bar, example study tasks, and two hand-written sample chat bubbles. They are fixed HTML, not model output. No student account, personal study history, backend, or AI API key is used.

## Code guide
- `.bar` and its child `<i>` form a visual progress indicator; the width is illustrative.
- `.chat` and `.chat.right` style the example conversation bubbles.
- `.rows` and `.row` display an example schedule.
- Root-page JavaScript filters project cards only. It does not power the study assistant.

## Responsible AI direction
A future implementation could connect a model through a server-side API, add user-controlled context, cite source material, and encourage students to verify suggestions. It should protect student data and provide a way to flag incorrect or unsuitable output. Never expose a private API key in browser JavaScript.

## References
- [UNESCO guidance for generative AI in education](https://unesdoc.unesco.org/ark:/48223/pf0000386693) — responsible-use context.
- [W3C guidance on cognitive accessibility](https://www.w3.org/TR/coga-usable/) — inclusive learning experience reference.
- [MDN Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) — web reference for a future server-backed integration.

Studywise is an independent educational concept, not endorsed by these organizations.
