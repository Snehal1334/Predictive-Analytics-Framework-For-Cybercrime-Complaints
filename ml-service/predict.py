from pathlib import Path
import joblib

MODEL_PATH = Path(__file__).parent / "models" / "complaint_model.joblib"
DEFAULT_MODEL_VERSION = "fallback-rules-v1"


class PredictionEngine:
    def __init__(self):
        self.model_loaded = MODEL_PATH.exists()
        self.bundle = joblib.load(MODEL_PATH) if self.model_loaded else None
        self.model_version = self.bundle.get("version", "unknown") if self.bundle else DEFAULT_MODEL_VERSION

    def predict(self, text, latitude=None, longitude=None):
        if self.bundle:
            category_model = self.bundle["category_model"]
            severity_model = self.bundle["severity_model"]
            category = category_model.predict([text])[0]
            severity = severity_model.predict([text])[0]
            category_confidence = max(category_model.predict_proba([text])[0])
            severity_confidence = max(severity_model.predict_proba([text])[0])
            confidence = (category_confidence + severity_confidence) / 2
        else:
            category, severity, confidence = self._fallback(text)
        escalation_risk = self._risk(text, severity, latitude, longitude)
        return {
            "category": category,
            "severity": severity,
            "escalation_risk": round(escalation_risk, 3),
            "confidence": round(float(confidence), 3),
            "model_version": self.model_version,
            "model_loaded": self.model_loaded,
        }

    def _fallback(self, text):
        lowered = text.lower()
        if any(word in lowered for word in ["upi", "bank", "wallet", "card", "investment"]):
            return "Financial Fraud", "HIGH", 0.62
        if any(word in lowered for word in ["otp", "password", "link", "login"]):
            return "Phishing", "MEDIUM", 0.58
        if any(word in lowered for word in ["profile", "instagram", "facebook", "blackmail"]):
            return "Social Media Abuse", "HIGH", 0.57
        return "Online Fraud", "MEDIUM", 0.5

    def _risk(self, text, severity, latitude, longitude):
        base = {"LOW": 0.2, "MEDIUM": 0.45, "HIGH": 0.72, "CRITICAL": 0.9}.get(severity, 0.45)
        lowered = text.lower()
        if any(word in lowered for word in ["blackmail", "threat", "minor", "suicide", "urgent"]):
            base += 0.15
        if latitude is not None and longitude is not None:
            base += 0.03
        return min(base, 0.98)
