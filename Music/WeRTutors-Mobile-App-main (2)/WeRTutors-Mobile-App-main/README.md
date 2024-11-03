# OPSC - POE

# WerTutors - Tutor Platform Application

WerTutors is an Android-based tutor platform built using Kotlin, designed to connect students with tutors. The app enables smooth interaction between tutors and students, offers booking features, and includes a review system for tutors.

## Technologies Used
- **Kotlin**: Programming language used for app development.
- **Firebase**: Backend service for authentication, data management, and offline support.
- **Gemini AI**: Integrated API for AI assistant features.
- **Google Sign-In**: Secure login option.
- **Biometric Authentication**: Added biometric login for enhanced security.
- **Push Notifications**: Keeps users informed of important updates and notifications.

## How to Run the App
1. **Using the Emulator**
   - Open Android Studio and load the project.
   - Connect your emulator by selecting AVD Manager from the toolbar.
   - Select a virtual device and click Run or press Shift+F10.
   - The app will be built and deployed to the emulator automatically.

2. **On Your Device**
   - Connect your Android device via USB.
   - Enable developer mode and USB debugging on the device.
   - Select your device from the target device list in Android Studio.
   - Click Run or press Shift+F10 to build and deploy the app to your device.

## Database
- **Firebase**: Used for managing user authentication, real-time data storage, user profiles, and offline access.

## API Features
- **Gemini AI Helper**: A smart assistant integrated into the platform.
- **Google Sign-In**: Easy and secure login option for users.

## Features
1. **Login**
   - Users can log in using email and password.
   - Option to log in using Google Sign-In.
   - Biometric Authentication: For added convenience, users can log in with biometric authentication (e.g., fingerprint or face recognition) if their device supports it.
   - Offline Login: Users can log into the app even without internet access once they’ve logged in at least once online.

2. **Register**
   - Users can register as either Tutors or Students.
   - Different registration requirements based on the role:
     - **Students**: Name, surname, email, password, confirm password, school.
     - **Tutors**: Name, surname, email, password, confirm password, phone number.

3. **Google Sign-In**
   - Integration with Firebase Authentication for Google login.

4. **Tutor Options**
   - Shows all available tutors on the platform.
   - Displays tutor information, including subjects taught and contact details.

5. **Review Section**
   - Students can submit reviews for tutors.
   - Students can view other users' reviews on a tutor.

6. **Settings**
   - Includes dark mode toggle.
   - Allows users to edit their profile information.
   - Multi-language Support: Users can select from multiple language options.

7. **Navigation Menu**
   - A navigation drawer allows easy movement throughout the app, including access to various features like tutor profiles, reviews, communication, etc.
   - Fixed Navigation Drawer: Improvements made for a smoother user experience.

8. **API Features**
   - Gemini AI Helper: An AI-powered assistant for both tutors and students.
   - Google Sign-In: Easy login with Google credentials.

9. **Session Time**
   - Tutors can record the session time (start and finish) of their tutoring sessions for accurate time tracking.

10. **Booking**
   - Users can schedule tutoring sessions with preferred tutors.

11. **Job Openings**
   - Shows available job openings where tutors can apply for tutoring positions.

12. **Communication**
   - A built-in chat feature allows users (both tutors and students) to communicate with each other within the app.

13. **Progress Tracking**
   - Users can track the progress of their marks over time, helping them monitor their learning outcomes and improvements.

14. **Push Notifications**
   - Users receive notifications for important updates, reminders, and announcements to stay up-to-date within the app.

## References
- [Firebase Google Sign-In Tutorial](https://youtu.be/WqIFBuWNY6o?si=EC9Gzu4NKtDsnaJM)
- [Tutor and Student Registration in Firebase](https://youtu.be/idbxxkF1l6k?si=yjNaOvlXR3aqokvF)
- [Gemini AI Integration in Android Apps](https://youtu.be/8Pv96bvBJL4?si=5EjZ2qwWLyZJEYnT)
- [Firebase Database for Session Time Tracking](https://youtu.be/EoJX7h7lGxM?si=x3rV_M0tvPuwttMQ)
- [Schedule Feature for Tutors](https://youtu.be/f9vnYb42nTs?si=ZRfx-NbIV0i2Noxw)
- [Implementing a Job Openings Section](https://youtu.be/VVXKVFyYQdQ?si=g7enVPcmej4Cd8ER)
- [Communication Features in Android](https://youtu.be/_cwhaOP0wbw?si=BL79xgJ7EPiokXj6)
- Codex, A.C. (2024) *Building an Android app with offline support using Room Persistence Library*, Reintech Media. Available at: [https://reintech.io/blog/building-android-app-offline-support-room-persistence-library](https://reintech.io/blog/building-android-app-offline-support-room-persistence-library) (Accessed: 02 November 2024).
- Android Developers (2024) *Implement Dark Theme: Views*, Android Developers. Available at: [https://developer.android.com/develop/ui/views/theming/darktheme](https://developer.android.com/develop/ui/views/theming/darktheme) (Accessed: 02 November 2024).
- Ahmed, W. (2024) *Creating a Multilingual Android Application*, Medium. Available at: [https://medium.com/@walidkhan345/creating-a-multilingual-android-application-f286a5bf3096](https://medium.com/@walidkhan345/creating-a-multilingual-android-application-f286a5bf3096) (Accessed: 02 November 2024).
- Android Developers (2024) *Show a Biometric Authentication Dialog: Identity*, Android Developers. Available at: [https://developer.android.com/identity/sign-in/biometric-auth](https://developer.android.com/identity/sign-in/biometric-auth) (Accessed: 02 November 2024).
- Jalaludin, M. (2018) *How to Create User Interface Login & Register with Android Studio*, Medium. Available at: [https://medium.com/muhamadjalaludin/how-to-create-user-interface-login-register-with-android-studio-34da847b05b2](https://medium.com/muhamadjalaludin/how-to-create-user-interface-login-register-with-android-studio-34da847b05b2) (Accessed: 02 November 2024).
- Google AI for Developers (2022) *Get Started with the Gemini API*, Google. Available at: [https://ai.google.dev/gemini-api/docs](https://ai.google.dev/gemini-api/docs) (Accessed: 02 November 2024).
- Anand, A. (n.d.) *Bottom Navigation Bar in Android using Kotlin*, GeeksforGeeks. Available at: [https://www.geeksforgeeks.org/bottom-navigation-bar-in-android-using-kotlin/](https://www.geeksforgeeks.org/bottom-navigation-bar-in-android-using-kotlin/) (Accessed: 02 November 2024).
- Anand, A. (2019) *Navigation Drawer in Android*, GeeksforGeeks. Available at: [https://www.geeksforgeeks.org/navigation-drawer-in-android/](https://www.geeksforgeeks.org/navigation-drawer-in-android/) (Accessed: 02 November 2024).
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) (no date) Google. Available at: [https://firebase.google.com/docs/cloud-messaging/](https://firebase.google.com/docs/cloud-messaging/) (Accessed: 02 November 2024).
- [YouTube Tutorial](https://www.youtube.com/watch?v=NbUfD_ZsPr4&t=1s)
