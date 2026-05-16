from app import app


def test_predict_requires_text():
    client = app.test_client()
    response = client.post("/api/v1/predict", json={"text": "short"})
    assert response.status_code == 400


def test_predict_returns_category():
    client = app.test_client()
    response = client.post("/api/v1/predict", json={"text": "Fake bank call stole money from my account"})
    body = response.get_json()
    assert response.status_code == 200
    assert "category" in body
    assert "severity" in body
    assert "escalation_risk" in body
    assert "confidence" in body
    assert "model_version" in body


def test_predict_alias_matches_api_contract():
    client = app.test_client()
    response = client.post("/predict", json={"text": "Suspicious login link asked for OTP and password"})
    assert response.status_code == 200
    assert response.get_json()["category"] in {"Phishing", "Financial Fraud", "Online Fraud", "Social Media Abuse", "Identity Theft"}
