# NextRole

A job-search organization platform. NextRole lets you track job applications through their full lifecycle — from draft to offer or rejection — store and manage resumes, browse and save job postings, and use optional AI-assisted tools to tailor a resume to a specific posting, generate interview-prep questions, and get feedback on a resume's structure and clarity.

> Solo-built, full-stack project. Not yet deployed — this repo is shared publicly so the code itself can be reviewed.

## Features

- **Application tracking** — a calendar-based view of every application's status, from draft through offer/rejection
- **Resume management** — upload, organize, and preview resumes
- **AI-assisted tools** — tailor a resume to a job posting, generate interview-prep questions, and get structural feedback on a resume
- **Job posting discovery** — browse and save postings added by yourself or others
- **Accounts and billing** — JWT-based auth with password reset, Free and Premium tiers via Stripe

## Tech stack

- **Frontend:** Angular (standalone components, signals, reactive forms), TypeScript, Tailwind CSS
- **Backend:** Java, Spring Boot, Spring Security (JWT)
- **Data:** PostgreSQL (JPA/Hibernate) for core application data, Amazon DynamoDB for feedback submissions
- **Cloud/AI:** AWS S3 (resume storage), AWS Bedrock (resume tailoring, interview prep, resume feedback)
- **Payments:** Stripe subscriptions and webhooks
- **Infra:** Docker Compose for local orchestration

## Status

Actively in development, built solo. No public deployment yet — run locally against your own database, AWS, and Stripe credentials.

## License

Copyright © 2026 Kenneth Fadojutimi. All rights reserved.

This repository is shared publicly for portfolio and code-review purposes only. The source code is proprietary — it may not be copied, modified, distributed, or used for any commercial or non-commercial purpose without explicit written permission from the owner. See [LICENSE](./LICENSE) for details.
