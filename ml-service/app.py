from flask import Flask, jsonify, request
from flask_cors import CORS
from predict import PredictionEngine

app = Flask(__name__)
CORS(app)
engine = PredictionEngine()


@app.get("/health")
def health():
    return jsonify({
        "status": "UP",
        "model_loaded": engine.model_loaded,
        "model_version": engine.model_version,
    })


@app.post("/api/v1/predict")
def predict():
    payload = request.get_json(silent=True) or {}
    text = payload.get("text", "")
    if not text or len(text.strip()) < 10:
        return jsonify({"message": "text must contain at least 10 characters"}), 400
    return jsonify(engine.predict(text, payload.get("latitude"), payload.get("longitude")))


@app.post("/predict")
def predict_alias():
    return predict()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
