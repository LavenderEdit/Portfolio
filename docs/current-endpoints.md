# Portfolio Hub API Current Endpoints

Baseline captured before the security-modernization implementation.

## Public

- `GET /` - service welcome/status response.
- `POST /api/auth/register` - email/password registration.
- `POST /api/auth/login` - email/password login.
- `GET /api/portfolios` - list public portfolios.
- `GET /api/portfolios/{slug}` - portfolio detail by slug.
- `GET /api/portfolios/{profileSlug}/projects/{projectSlug}` - public project detail.
- `POST /api/portfolios/{slug}/contact` - public contact form.
- `GET /api/skills` - global skill lookup when exposed by security config.

## Authenticated User

- `GET /api/me/profile`
- `PUT /api/me/profile`
- `PUT /api/me/settings/contact-email`
- CRUD-style endpoints under `/api/me/experience`, `/api/me/education`, `/api/me/social-links`, `/api/me/projects`, `/api/me/certificates`.
- Batch skill-category and skill endpoints under `/api/me/skill-categories`.
- Upload endpoints under `/api/me/upload`.

## Admin

- `POST /api/admin/profiles/{profileId}/toggle-collaborator`

## Documentation

- `/v3/api-docs/**` and `/swagger-ui/**` are currently dev-only after hardening.
