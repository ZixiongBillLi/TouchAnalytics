import os
import numpy as np
import firebase_admin
from joblib import dump, load  # For saving and loading the classifier
from sklearn.svm import SVC  # Import a classifier
from firebase_admin import credentials, db
from flask import Flask, request, jsonify

# The Minimum number of training samples required for a user to be included in training.
MIN_TARGET_SAMPLES = 50
# --- Firebase setup ---
FIREBASE_CRED_PATH = 'firebase_service_key.json'
FIREBASE_DB_URL = 'https://swen-549-touchanalytics-default-rtdb.firebaseio.com/'

credentials = credentials.Certificate(FIREBASE_CRED_PATH)
firebase_admin.initialize_app(credentials, {'databaseURL': FIREBASE_DB_URL})

# file for saving the global classifier model.
GLOBAL_MODEL_FILE = "global_classifier.pkl"

# Define the list of expected feature keys.
FEATURE_KEYS = [
    "averageDirection", "averageVelocity", "directionEndToEnd", "midStrokeArea",
    "pairwiseVelocityPercentile", "startX", "startY", "stopX", "stopY", "strokeDuration"
]


def train_and_save_global_classifier():
    """
    Gather training data from Firebase for all users that have at least MIN_TARGET_SAMPLES,
    train a multi-class classifier (each sample labeled with its userID), and save the model.
    """
    all_users_data = db.reference('/').get()  # Load data from Firebase.
    if all_users_data is None:
        raise ValueError("No data found in database")

    X = []  # List of feature vectors.
    y = []  # Corresponding labels (user IDs).
    user_sample_count = {}  # Count samples per user.

    # Iterate over each user in the database.
    for uid_str, recordings in all_users_data.items():
        try:
            uid = int(uid_str)
        except ValueError:
            continue  # Skip keys that are not valid user IDs.

        if not isinstance(recordings, dict):
            continue  # Expecting a dictionary of recordings.

        for rec_key, features in recordings.items():
            # Ensure that this record contains all required features.
            if not all(k in features for k in FEATURE_KEYS):
                continue

            try:
                sample = [float(features[k]) for k in FEATURE_KEYS]
            except ValueError:
                continue  # Skip records with non-numeric data.

            X.append(sample)
            y.append(uid)
            user_sample_count[uid] = user_sample_count.get(uid, 0) + 1

    # Filter to include only samples from users that have enough samples.
    valid_X = []
    valid_y = []
    for sample, label in zip(X, y):
        if user_sample_count[label] >= MIN_TARGET_SAMPLES:
            valid_X.append(sample)
            valid_y.append(label)

    if len(valid_y) == 0:
        raise ValueError("Not enough data to train the global model.")

    model = SVC(kernel='linear')
    model.fit(np.array(valid_X), np.array(valid_y))  # Train the multi-class classifier.
    dump(model, GLOBAL_MODEL_FILE)  # Save the trained global model.
    print(f"Model Retrained with {len(user_sample_count)} users")
    # Calculate training accuracy.
    train_accuracy = model.score(valid_X, valid_y)
    print(f"Training Accuracy: {train_accuracy * 100}%", )
    return model


def load_or_train_global_model(target_user):
    """
    Load the global model if available. If the model does not exist or if the target_user is not
    among the model's classes, retrain the model.
    """
    model_needs_training = False
    if os.path.exists(GLOBAL_MODEL_FILE):
        try:
            model = load(GLOBAL_MODEL_FILE)
            # Check if the target_user is in the classifier's known classes.
            if not hasattr(model, "classes_") or target_user not in model.classes_:
                model_needs_training = True
        except Exception:
            model_needs_training = True
    else:
        model_needs_training = True

    if model_needs_training:
        model = train_and_save_global_classifier()
        # After training, if the target user still isn’t included, then there isn’t enough data.
        if not hasattr(model, "classes_") or target_user not in model.classes_:
            raise ValueError(
                f"Not enough data to train the model for user {target_user}."
            )
    return model


# --- Flask app setup ---
app = Flask(__name__)


@app.route('/authenticate/<int:userID>', methods=['POST'])
def authenticate(userID):
    print(f"Incoming Request: {request.get_data(as_text=True)}")
    # Parse JSON from the request body.
    req_data = request.get_json()
    if not req_data:
        resp = {"message": "Invalid features provided"}
        print(f"Outgoing Response: {resp}")
        return jsonify(resp), 400

    # Check that all expected features are provided in the JSON.
    missing_keys = [key for key in FEATURE_KEYS if key not in req_data]
    if missing_keys:
        resp = {"message": f"Missing feature(s): {missing_keys}"}
        print(f"Outgoing Response: {resp}")
        return jsonify(resp), 400

    # Build the feature vector from the request data.
    try:
        new_sample = [float(req_data[key]) for key in FEATURE_KEYS]
    except ValueError:
        resp = {"message": "Feature values must be numeric"}
        print(f"Outgoing Response: {resp}")
        return jsonify(resp), 400

    try:  # --- Load or retrain the global classifier if needed ---
        model = load_or_train_global_model(userID)
    except ValueError as ve:
        resp = {"message": str(ve)}
        print(f"Outgoing Response: {resp}")
        return jsonify(resp), 400

    # Predict the user for the new sample.
    new_sample_arr = np.array(new_sample).reshape(1, -1)
    prediction = model.predict(new_sample_arr)[0]

    # Return response: "match=True" if the predicted user equals the provided userID.
    response = {
        "match": True if prediction == userID else False,
        "message": "Matched" if prediction == userID else "Not matched",
    }
    print(response, prediction)
    print(f"Outgoing Response: {response}")
    return jsonify(response, ), 200


@app.route('/delete/<int:userID>', methods=['DELETE'])
def delete_user(userID):
    print(f"Incoming Request to delete user: {userID}")
    try:
        # Delete user data from Firebase
        db.reference(f'/{userID}').delete()
        print(f"Deleted user {userID} data from Firebase.")
        
        # Delete the global model file to remove the user from the model
        if os.path.exists(GLOBAL_MODEL_FILE):
            os.remove(GLOBAL_MODEL_FILE)
            print(f"Deleted {GLOBAL_MODEL_FILE}.")
            
        # Try to retrain the model immediately to reflect the deletion
        try:
            train_and_save_global_classifier()
            print("Successfully retrained global model.")
        except Exception as e:
            # This can happen if there isn't enough data left to train the model
            print(f"Could not retrain model after deletion: {e}")

        return jsonify({"message": f"User {userID} deleted successfully."}), 200
    except Exception as e:
        print(f"Error deleting user {userID}: {e}")
        return jsonify({"message": f"Error deleting user: {str(e)}"}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)  # Run the Flask app on port 5001 (adjust host/port as needed).
