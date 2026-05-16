# Model Artifact

Run `python train_model.py` to generate `complaint_model.joblib`.

The model is a scikit-learn pipeline using `TfidfVectorizer` and `RandomForestClassifier`, persisted with `joblib`.

The Docker image trains the starter model during build with Python 3.12. The local Flask service also has a deterministic fallback so `/api/v1/predict` remains available before a model artifact exists.
