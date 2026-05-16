# API Documentation

Swagger UI is available at `/swagger-ui.html` when the backend is running.

## Authentication Flow

1. Register or login to receive `accessToken` and `refreshToken`.
2. Send `Authorization: Bearer <accessToken>` for protected endpoints.
3. Use `/auth/refresh-token` before the access token expires.

## Complaint Payload

```json
{
  "title": "UPI fraud complaint",
  "description": "A caller pretended to be bank support and stole money through UPI after asking for OTP details.",
  "incidentDate": "2026-05-14",
  "address": "Connaught Place",
  "city": "Delhi",
  "state": "Delhi",
  "postalCode": "110001",
  "latitude": 28.6315,
  "longitude": 77.2167,
  "severity": "HIGH"
}
```

The backend stores the complaint, calls the Flask ML service, saves prediction metadata, and returns the enriched complaint response.
