# LearnHub — Software Engineering Learning App

## How to Build & Run

### Requirements
- Android Studio Hedgehog (2023.1.1) or newer — FREE at https://developer.android.com/studio
- Android SDK 24+ (auto-installed by Android Studio)
- JDK 8 or higher

### Steps to Build APK
1. Open Android Studio
2. Click **"Open"** → select this `SoftEngApp` folder
3. Wait for Gradle sync to complete (~2 min, first time downloads dependencies)
4. Click **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

### Steps to Run on Emulator
1. In Android Studio → **Device Manager** → **Create Device**
2. Choose Pixel 8, API 34
3. Press the **▶ Run** button

### Steps to Run on Real Device
1. Enable **Developer Options** on your Android phone
2. Enable **USB Debugging**
3. Connect phone via USB
4. Press **▶ Run** in Android Studio

## App Structure
```
SoftEngApp/
├── app/src/main/
│   ├── AndroidManifest.xml          ← App config, all 8 activities declared
│   ├── java/com/softeng/learnhub/
│   │   ├── activities/
│   │   │   ├── SplashActivity.java  ← Entry point with animations
│   │   │   ├── MainActivity.java    ← Home dashboard
│   │   │   ├── CourseListActivity.java  ← Browse 5 courses
│   │   │   ├── CourseDetailActivity.java ← Lessons + TTS
│   │   │   ├── QuizActivity.java    ← 10-question quiz with timer
│   │   │   ├── ResultActivity.java  ← Score + feedback
│   │   │   ├── LeaderboardActivity.java ← Best scores
│   │   │   ├── SettingsActivity.java ← Preferences
│   │   │   └── ProfileActivity.java ← User profile
│   │   ├── adapters/
│   │   │   └── CourseAdapter.java   ← RecyclerView adapter
│   │   ├── models/
│   │   │   ├── Course.java
│   │   │   ├── Lesson.java
│   │   │   ├── Question.java
│   │   │   └── QuizResult.java
│   │   └── utils/
│   │       ├── DataRepository.java  ← All 5 courses, 4 lessons each, 10 questions each
│   │       └── PreferencesManager.java ← SharedPreferences wrapper
│   └── res/
│       ├── layout/                  ← All XML layouts (ConstraintLayout + Material)
│       ├── drawable/                ← Vector icons, backgrounds
│       ├── values/                  ← colors.xml, strings.xml, themes.xml
│       └── anim/                    ← Slide and bounce animations
```

## Features Checklist (Grading Rubric)
- ✅ 8 Activities with explicit & implicit Intents
- ✅ Back-stack managed (finish(), FLAG_ACTIVITY_CLEAR_TOP)
- ✅ ConstraintLayout + Material Design components
- ✅ Custom drawables (vector icons, badges, shapes)
- ✅ SharedPreferences (scores, settings, profile)
- ✅ Text-to-Speech hardware feature (reads lessons aloud)
- ✅ Implicit Intent: ACTION_SEND (share), ACTION_VIEW (browser)
- ✅ RecyclerView with ViewHolder pattern
- ✅ 30-second countdown timer per question
- ✅ Color feedback: green=correct, red=wrong
- ✅ Answer explanations after each question
- ✅ MVC architecture pattern
- ✅ 5 courses × 4 lessons × 10 questions = 50 quiz questions
