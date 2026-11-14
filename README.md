# HappyMinds 🌟

A South African primary school learning app for Grades 1–7, built for Android. **HappyMinds** gives learners access to structured CAPS-aligned curriculum notes across four core subjects — built with offline curriculum access and cloud-synced user progress via Firebase.

---

## 📋 Table of Contents

- [About the App](#about-the-app)
- [Key Features](#key-features)
- [Tech Stack & Architecture](#tech-stack--architecture)
- [Project Structure](#project-structure)
- [Database & Firebase Setup](#database--firebase-setup)
- [Firestore Security Rules](#firestore-security-rules)
- [User Roles & Registration Flow](#user-roles--registration-flow)
- [Curriculum Overview](#curriculum-overview)
- [Build & Run Instructions](#build--run-instructions)

---

## 📱 About the App

**HappyMinds** is a parent-managed learning companion for South African primary school learners in **Grades 1 to 7**. 

Parents register an account or sign in using **Google Sign-In**, complete their child's profile (name, age, and grade), and automatically access structured curriculum notes tailored for that grade level. Dedicated portals are also included for **Admins** (to review and approve registrations) and **Volunteers** (to submit helper applications).

---

## 🚀 Key Features

### For Learners
- 📚 **Structured Lesson Notes**: Comprehensive notes for 84 lessons across 4 core subjects (Maths, English, Natural Science, Life Skills).
- 📊 **Progress Tracking**: Real-time progress breakdown per lesson (0% to 100% complete).
- 🔍 **Search & Filtering**: Search lessons by keyword or filter by subject chips.
- 🎯 **Offline Curriculum**: All lesson notes and practice questions are available offline instantly.

### For Parents
- 🔐 **Flexible Sign-In**: Email/password authentication or **Continue with Google** via Android Credential Manager.
- 👶 **Child Profile Management**: Easily set up and update child details (name, age, grade 1–7).
- 📈 **Progress Dashboard**: View completed lessons, badges, and learning streak statistics.
- 💳 **Support & Donations**: Support HappyMinds via integrated PayFast payments.

### For Volunteers
- 🤝 **Volunteer Application Form**: Submit volunteer applications with specialized skills and help areas for admin review.

### For Admins
- 🛡️ **Admin Portal**: Dedicated admin login verifying permissions against Firestore.
- ✅ **Approval Management**: Review, approve, or decline pending learner and volunteer applications.
- 💰 **Donation Monitoring**: Overview of total donations and user statistics.

---

## 🛠️ Tech Stack & Architecture

- **Language & Runtime**: Kotlin (JVM 21)
- **UI & Navigation**: AndroidX ViewBinding, Material Design 3, ConstraintLayout, RecyclerView, CardView, Navigation Component
- **Authentication**: Firebase Authentication (Email/Password & Google Sign-In via `androidx.credentials` + `googleid`)
- **Database**: Cloud Firestore
- **Asynchronous Processing**: Kotlin Coroutines & `lifecycleScope`
- **Build System**: Gradle Version Catalogs (`libs.versions.toml`)

---

## 📁 Project Structure

```
HappyMinds/
├── app/
│   ├── src/main/java/com/happyminds/app/
│   │   ├── data/
│   │   │   ├── Models.kt              # Data models, status enums & CAPS curriculum definitions
│   │   │   ├── UserRepository.kt      # Firebase Auth & Firestore user operations
│   │   │   ├── DonationRepository.kt  # PayFast & donation Firestore operations
│   │   │   └── FirestoreMappers.kt    # DocumentSnapshot extension mappers
│   │   ├── ui/
│   │   │   ├── admin/                 # Admin Dashboard & adapters
│   │   │   ├── auth/                  # Login, Register, Google Sign-In & Volunteer screens
│   │   │   ├── home/                  # HomeFragment & dashboard view
│   │   │   ├── learn/                 # LearningCenterFragment & LessonDetailActivity
│   │   │   ├── progress/              # ProgressFragment & statistics
│   │   │   ├── profile/               # ProfileFragment, SupportFragment & PayFast Checkout
│   │   │   └── community/             # CommunityFragment & event updates
│   │   └── MainActivity.kt            # Bottom navigation container
│   └── src/main/res/                  # Layouts, drawables, values & navigation graph
├── firestore.rules                    # Cloud Firestore security rules
├── README.md
└── build.gradle.kts
```

---

## 🗄️ Database & Firebase Setup

HappyMinds uses **Firebase Authentication** and **Cloud Firestore**:

- **Firebase Authentication**: Manages secure email/password accounts and Google OAuth sessions.
- **Cloud Firestore**:
  - `users/{uid}`: Holds user profiles, roles (`isVolunteer`), and account statuses (`PENDING`, `APPROVED`, `REJECTED`).
  - `users/{uid}/progress/{lessonId}`: Tracks progress percentage and timestamps for each lesson.
  - `admins/{uid}`: Stores admin authorization documents (`admins/{adminUid}`).
  - `donations/{donationId}`: Stores PayFast donation transaction records.

---

## 🔐 Firestore Security Rules

Deploy the contents of [`firestore.rules`](file:///C:/Users/User/AndroidStudioProjects/HappyMinds%20app/firestore.rules) to your Firebase Console under **Firestore Database -> Rules**:

```firestore
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {

    function isSignedIn() {
      return request.auth != null;
    }

    function isOwner(userId) {
      return isSignedIn() && request.auth.uid == userId;
    }

    function isAdmin() {
      return isSignedIn() && exists(/databases/$(database)/documents/admins/$(request.auth.uid));
    }

    // Users Collection
    match /users/{userId} {
      allow create: if isOwner(userId);
      allow read, update: if isOwner(userId) || isAdmin();
      allow delete: if isAdmin();

      match /progress/{lessonId} {
        allow read, write: if isOwner(userId) || isAdmin();
      }
    }

    // Admins Collection
    match /admins/{adminId} {
      allow read: if isSignedIn();
      allow write: if false;
    }

    // Donations Collection
    match /donations/{donationId} {
      allow read: if isSignedIn();
      allow create, update: if isSignedIn() || isAdmin();
    }
  }
}
```

---

## 👥 User Roles & Registration Flow

| Role | Access | Login Method |
| :--- | :--- | :--- |
| **Parent / Learner** | CAPS Curriculum, lesson notes, child profile & progress | Email/Password or Google Sign-In |
| **Volunteer** | Application submission & skill details | Volunteer link on Login screen |
| **Admin** | Approve/reject registrations, view user & donation stats | Admin Login link on Login screen |

### Account Lifecycle Flow
1. **Parent/Volunteer Registers** → Status set to `PENDING` (Google accounts auto-set to `APPROVED`).
2. **Admin Review** → Admin logs into Admin Dashboard and approves or declines the application.
3. **Login & Profile Setup** → Approved accounts log in and set up child grade details to access the curriculum.

---

## 📖 Curriculum Overview

The CAPS-aligned curriculum is defined in [`Models.kt`](file:///C:/Users/User/AndroidStudioProjects/HappyMinds%20app/app/src/main/java/com/happyminds/app/data/Models.kt).

- **Grades Supported**: Grade 1 through Grade 7.
- **Core Subjects**:
  - 🔢 Mathematics
  - 📖 English
  - 🔬 Natural Science
  - 🎨 Life Skills
- **Total Content**: 12 lessons per grade × 7 grades = **84 structured lessons**.
- **Lesson Structure**: Learning objectives, core notes, practice questions, and self-check answers.

---

## 🏗️ Build & Run Instructions

1. Clone or open the project in **Android Studio**.
2. Connect your Firebase project by placing `google-services.json` inside the `app/` folder.
3. Deploy the [`firestore.rules`](file:///C:/Users/User/AndroidStudioProjects/HappyMinds%20app/firestore.rules) in Firebase Console.
4. Build and run the app:
   ```bash
   ./gradlew assembleDebug
   ```
