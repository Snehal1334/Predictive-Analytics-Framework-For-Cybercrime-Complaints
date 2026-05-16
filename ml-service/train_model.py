from pathlib import Path
import joblib
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline

ROOT = Path(__file__).parent
MODEL_PATH = ROOT / "models" / "complaint_model.joblib"
MODEL_VERSION = "random-forest-tfidf-v1"

data = [
    ("Received fake bank call and lost money through UPI transfer", "Financial Fraud", "HIGH"),
    ("Credit card details stolen from phishing website", "Financial Fraud", "HIGH"),
    ("Fake investment app is not allowing withdrawal", "Financial Fraud", "HIGH"),
    ("Suspicious link asked for OTP and password", "Phishing", "MEDIUM"),
    ("Email login page copied bank website", "Phishing", "MEDIUM"),
    ("Unknown person created fake social media profile", "Social Media Abuse", "MEDIUM"),
    ("Blackmail on Instagram using edited photos", "Social Media Abuse", "HIGH"),
    ("Online marketplace seller took payment but never delivered", "Online Fraud", "MEDIUM"),
    ("Threatening messages and repeated harassment online", "Social Media Abuse", "HIGH"),
    ("Mobile wallet fraud after sharing screen with caller", "Financial Fraud", "HIGH"),
    ("Lottery SMS link captured personal details", "Phishing", "LOW"),
    ("Gaming account hacked and items sold", "Identity Theft", "MEDIUM"),
    ("Aadhaar and identity documents used for fake account", "Identity Theft", "HIGH"),
    ("Fake courier link asked me to pay customs charge and captured my card", "Phishing", "MEDIUM"),
    ("Someone used my PAN card to open a loan account", "Identity Theft", "HIGH"),
    ("Repeated cyber stalking and abusive messages from unknown account", "Social Media Abuse", "HIGH"),
    ("Online job portal demanded registration fee and disappeared", "Online Fraud", "MEDIUM"),
    ("Ransomware locked office files and demanded crypto payment", "Ransomware", "CRITICAL"),
    ("Company email compromised and vendor payment redirected", "Business Email Compromise", "CRITICAL"),
]


def train():
    frame = pd.DataFrame(data, columns=["text", "category", "severity"])
    category_model = Pipeline([
        ("tfidf", TfidfVectorizer(ngram_range=(1, 2), min_df=1, stop_words="english")),
        ("classifier", RandomForestClassifier(
            n_estimators=250,
            max_depth=None,
            min_samples_leaf=1,
            class_weight="balanced",
            random_state=42,
            n_jobs=-1,
        )),
    ])
    severity_model = Pipeline([
        ("tfidf", TfidfVectorizer(ngram_range=(1, 2), min_df=1, stop_words="english")),
        ("classifier", RandomForestClassifier(
            n_estimators=250,
            max_depth=None,
            min_samples_leaf=1,
            class_weight="balanced",
            random_state=84,
            n_jobs=-1,
        )),
    ])
    category_model.fit(frame["text"], frame["category"])
    severity_model.fit(frame["text"], frame["severity"])
    MODEL_PATH.parent.mkdir(parents=True, exist_ok=True)
    joblib.dump({
        "version": MODEL_VERSION,
        "algorithm": "RandomForestClassifier",
        "feature_extractor": "TfidfVectorizer",
        "category_model": category_model,
        "severity_model": severity_model,
        "labels": {
            "categories": sorted(frame["category"].unique().tolist()),
            "severities": sorted(frame["severity"].unique().tolist()),
        },
    }, MODEL_PATH)
    print(f"Saved model to {MODEL_PATH}")


if __name__ == "__main__":
    train()
