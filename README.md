# Customer Feedback App

A mobile application that allows businesses to collect, manage, and respond to customer feedback. Built with Kotlin and Jetpack Compose for modern Android development.

![Customer Feedback App](https://via.placeholder.com/800x400?text=Customer+Feedback+App)

## Features

- **User Authentication**: Secure login and registration system
- **Role-Based Access**: 
  - **User Portal**: Submit, view, edit, and delete their own feedback
  - **Admin Dashboard**: View and manage all user feedback
- **Feedback Management**:
  - Create new feedback entries
  - Edit existing feedback
  - Delete unwanted feedback
  - View feedback history
- **Real-time Database**: Powered by Firebase for seamless data synchronization
- **Modern UI**: Built with Jetpack Compose for a fluid, responsive interface

## Technologies Used

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Realtime Database
- MVVM Architecture
- Navigation Components

## Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 11 or newer
- Android device or emulator running Android 7.0 (API level 24) or higher
- Google Firebase account

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/Customer-Feedback-App.git
cd Customer-Feedback-App
```

### 2. Firebase Setup

1. Create a new Firebase project at [https://console.firebase.google.com/](https://console.firebase.google.com/)
2. Add an Android app to your Firebase project:
   - Use the package name: `com.shriram.customerfeedback`
   - Download the `google-services.json` file
3. Place the `google-services.json` file in the app/ directory
4. Enable Authentication in Firebase console and set up Email/Password sign-in method
5. Set up Realtime Database in Firebase console

### 3. Build and Run

Open the project in Android Studio:

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository and click "Open"
4. Wait for Gradle to sync
5. Connect an Android device or start an emulator
6. Click the "Run" button in Android Studio

## How to Use the App

### User Access

1. **Registration**:
   - Launch the app
   - Click on "Register" on the login screen
   - Fill in your email, password, and username
   - Click "Register" to create your account

2. **Login**:
   - Enter your registered email and password
   - Click "Login" to access the user dashboard

3. **Submit Feedback**:
   - From the user dashboard, click "Add Feedback"
   - Enter your feedback text
   - Click "Submit" to save your feedback

4. **Manage Your Feedback**:
   - View all your submitted feedback on the dashboard
   - Click on any feedback item to edit or delete it
   - Make changes and click "Update" to save edits

### Admin Access

1. **Login as Admin**:
   - Launch the app
   - Use the admin credentials:
     - Username: `admin`
     - Password: `admin`

2. **Admin Dashboard**:
   - View all user feedback in one place
   - See details including username and timestamp
   - Click on any feedback to edit or delete it

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Contact

For any questions or suggestions, please open an issue on GitHub or contact the repository owner. 