# Predictive Analytics Framework for Cybercrime Complaints

Enterprise-grade cybercrime complaint management and predictive analytics platform.

## Services

- `backend`: Java 17, Spring Boot 3, Spring Security JWT, JPA/Hibernate, MySQL, Swagger.
- `frontend`: React, Tailwind CSS, React Query, Recharts, Leaflet maps.
- `ml-service`: Flask, scikit-learn TF-IDF + Logistic Regression pipeline.
- `database`: MySQL bootstrap and seed notes.
- `postman`: API collection for quick testing.

## Quick Start

```bash
docker compose up --build
```

Default URLs:

- Frontend: `http://localhost`
- Backend API: `http://localhost:8080/api/v1`
- Swagger: `http://localhost:8080/swagger-ui.html`
- ML service: `http://localhost:5001/health`

Seed admin:

- Email: `admin@cyber.local`
- Password: `Admin@12345`

## Local Development

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

ML service:

```bash
cd ml-service
pip install -r requirements.txt
python train_model.py
python app.py
```

## Architecture

The project is organized as a micro-service friendly monorepo. The Spring Boot backend follows layered clean architecture: controllers expose `/api/v1` REST endpoints, services contain business logic, repositories isolate persistence, DTOs define API contracts, mappers adapt entities to responses, and global exception handling normalizes errors. The Flask service owns ML inference behind a separate REST API so it can scale independently.

## Backend API

Authentication:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh-token`
- `POST /api/v1/auth/forgot-password`

Complaints:

- `POST /api/v1/complaints`
- `GET /api/v1/complaints`
- `GET /api/v1/complaints/{id}`
- `PUT /api/v1/complaints/{id}`
- `DELETE /api/v1/complaints/{id}`

Analytics:

- `GET /api/v1/analytics/dashboard`
- `GET /api/v1/admin/categories`

## Environment

Copy each `.env.example` and configure secrets for production. Use a long random `JWT_SECRET`, lock CORS origins to the frontend domain, and place database credentials in your deployment secret manager.

## Testing

```bash
cd backend && mvn test
cd frontend && npm install && npm run test
cd ml-service && pip install -r requirements.txt && pytest
```

## Deployment

Dockerfiles are included for each service, plus `docker-compose.yml`, `railway.json`, and AWS notes in `docs/AWS_DEPLOYMENT.md`.
