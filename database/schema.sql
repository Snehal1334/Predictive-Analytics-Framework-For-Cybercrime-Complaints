CREATE DATABASE IF NOT EXISTS cybercrime_analytics;
USE cybercrime_analytics;

-- Hibernate creates the development tables. Recommended production indexes:
-- users(email)
-- complaints(status, severity)
-- complaints(incident_date)
-- locations(latitude, longitude)
