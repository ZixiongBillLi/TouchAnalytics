# TouchAnalytics

An Android app for **stroke-based behavioral authentication** inspired by Touchalytics.  
The app collects vertical swipe behavior from a chat-style interface, stores enrollment samples in Firebase Realtime Database, and uses a local Flask ML server to verify whether a new swipe matches the current user.

## Project Overview

This project is a course prototype for touch-based authentication.  
Instead of requiring a password or a fingerprint, the app uses **how a user swipes on the screen** as a behavioral biometric signal.

The current MVP flow is:

1. The user enters a numeric **User ID**
2. The user enters the app and navigates to a chat-based interface
3. The app collects swipe strokes during normal scrolling
4. The first 50 valid strokes are used for **enrollment**
5. After enrollment, new strokes are sent to a local ML server for **verification**
6. The app shows how many results are **Match** and **Not Match**

## Current UI / MVP Scope

The current MVP is focused on the authentication flow, not on building a full messaging platform.

### Included screens
- **Login screen**
  - numeric User ID input
- **Chat list**
  - list of mock chat partners
- **Chat view**
  - read-only incoming long messages
  - designed to allow repeated vertical scrolling for swipe collection

### Current mode behavior
- **Enrollment Mode**
  - shows current user ID
  - shows how many strokes have been collected out of 50
- **Verification Mode**
  - shows the number of green **Match**
  - shows the number of red **Not Match**

### Important note
The chat content is currently **read-only** for MVP.  
The purpose of the chat view is to provide a natural scrolling surface for swipe collection.  
Sending messages is not required for the current version.

---

## Tech Stack

- **Android Studio**
- **Kotlin**
- **Jetpack Compose**
- **Firebase Realtime Database**
- **Retrofit**
- **Local Flask ML server**
- `StrokeUtils.kt` for stroke feature extraction

---

## Repository Structure

At the time of writing, this repository contains the Android app module and standard Gradle project files. The repo currently includes:
- `app/`
- `gradle/`
- top-level Gradle build files
- Firebase config file (`google-services.json`) inside `app/` :contentReference[oaicite:1]{index=1}

A simplified structure:

```text
TouchAnalytics/
├── app/
│   ├── src/
│   ├── build.gradle.kts
│   ├── google-services.json
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```
## Prerequisites

Before running the project, make sure you have:

- Android Studio installed
- A working Android emulator or a physical Android device
- Internet access for Firebase
- A local machine that can run the Flask ML server
- The Android device and the Flask server machine on the same network if using a physical device

---

## Firebase Configuration

This app uses Firebase Realtime Database to store enrollment stroke data.

### Firebase Realtime Database URL

```text
https://swen-549-touchanalytics-default-rtdb.firebaseio.com/
```

### Notes

- Enrollment samples are stored in Firebase under the current numeric user ID
- Firebase must be accessible for enrollment to work correctly
- The project includes a `google-services.json` file in the `app/` module

---

## Persistent ML Server Configuration

This project uses a persistent Flask ML server for verification.

The ML server is hosted on AWS Elastic Beanstalk.

### Notes

If logcat is encountering an error with hostname on swipe, ensure that the emulator has an active internet connection.
Emulator may need to be restarted and cold booted.
## Local ML Server Configuration

This project may also use a local Flask ML server for verification.

### Port

```text
5001
```

### Important Note About the IP Address

The Flask server IP address may change each time the server starts.  
Please check the Flask terminal output every time before running the Android app.

Example Flask output:

```text
Running on http://127.0.0.1:5001
Running on http://129.21.40.25:5001
```

### Which Address Should Be Used?

- Do **not** use `127.0.0.1` for a physical Android device
- Use the current local network IP shown by Flask, for example:

```text
http://129.21.40.25:5001/
```

### Why?

- `127.0.0.1` means "this machine itself"
- On a phone, `127.0.0.1` refers to the phone, not the laptop/server machine
- The Android app must point Retrofit to the current local network IP

---

## API Endpoints

The Android app communicates with the Flask ML server using Retrofit.

### Required Authentication Endpoint

```text
POST /authenticate/{userID}
```

This endpoint is used in **Verification Mode**.

### Optional Reset/Delete Endpoint

```text
DELETE /delete/{userID}
```

This endpoint can be used to reset a user's enrollment data and start over.

---

## How to Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/ZixiongBillLi/TouchAnalytics.git
cd TouchAnalytics
```

### 2. Open in Android Studio

Open the project folder in Android Studio.

### 3. Sync Gradle

Let Android Studio finish Gradle sync.

If Gradle version issues appear, use the versions configured in the project and make sure Android Studio is using a compatible Android Gradle Plugin / Gradle combination.

### 4. Confirm Firebase Is Enabled

Make sure the Firebase Realtime Database is accessible and the Firebase configuration is valid.

### 5. Start the Flask ML Server

Run the local Flask server on your machine.  
When it starts, note the current local network IP address and confirm that the server is running on port `5001`.

Example:

```text
http://129.21.40.25:5001
```

### 6. Update the Android App Base URL If Needed

If the local IP changes, update the Retrofit base URL in the Android project so it points to the current Flask server address.

Example:

```text
http://129.21.40.25:5001/
```

If your code stores the base URL in a dedicated file such as `RetrofitClient.kt` or a network config file, update it there before running the app.

### 7. Run the Android App

Run the app on:

- an emulator, or
- a physical Android device

If using a physical device:

- the phone and the Flask server machine must be on the same network
- the app must use the current Flask server IP address, not `127.0.0.1`

---

## Suggested Test Flow for Grading

### New User / Enrollment Test

1. Launch the app
2. Enter a numeric User ID
3. Navigate into a chat view
4. Scroll repeatedly through the long read-only messages
5. Confirm the app shows **Enrollment Mode**
6. Continue until the enrollment count reaches `50`

### Verification Test

1. After enrollment completes, continue scrolling
2. Confirm the app switches to **Verification Mode**
3. Verify that the app sends new stroke features to the Flask server
4. Check that the UI updates:
   - green **Match**
   - red **Not Match**

### Reset Test (If Implemented)

1. Trigger reset / re-enrollment
2. Confirm the user's enrollment data is cleared
3. Confirm the app returns to Enrollment Mode

---

## Important Notes for Graders

- This is a course prototype / MVP
- The main focus is the touch authentication workflow
- The chat interface is intentionally simple and currently read-only
- The app is not intended to be a full messaging platform in the current version
- The Flask server IP may change between runs, so the local server address may need to be updated before testing

---

## Troubleshooting

### 1. App Cannot Reach ML Server

Check:

- Is the Flask server running?
- Is the app using the correct current local IP?
- Are the phone and server on the same network?
- Are you accidentally using `127.0.0.1` on a physical device?

### 2. Verification Does Not Work

Check:

- Has the user already collected at least 50 enrollment samples?
- Is Firebase storing the enrollment data correctly?
- Is the Flask server running on port `5001`?
- Is the Retrofit base URL correct?

### 3. Enrollment Does Not Progress

Check:

- Firebase connectivity
- valid stroke capture
- whether the app is successfully saving enrollment data under the current user ID

### 4. Gradle Sync Problems

Check:

- Android Studio version
- Android Gradle Plugin version
- Gradle wrapper version
- compatibility between Android Studio and AGP

---

## Future Work

Possible future extensions include:

- richer chat data management
- message sending
- more mock users and conversations
- improved reset / re-enrollment flow
- better verification feedback and usability polish

---

## Authors

Team members are contributing on separate branches and merging features into `main` as the project evolves.






