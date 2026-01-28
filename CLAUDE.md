# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Releaf** is a native Android stress and anxiety management app (ported from Nuxt.js). It features 18 relaxation techniques with interactive animations, Firebase integration, user progress tracking, and multi-language support (FR/EN/ES/ZH).

## Build and Development Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run all unit tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.releaf.app.ExampleUnitTest"

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean and rebuild
./gradlew clean assembleDebug

# Build release (ProGuard minification enabled)
./gradlew assembleRelease
```

## Architecture

### Tech Stack
- **Kotlin** (Java 17) + Jetpack Compose + Material 3
- **Room Database** (local) + **Firebase** (Auth, Firestore, Storage, Analytics)
- **Coroutines & Flows** for async operations
- **Box2D** physics engine for stress ball interaction
- **Navigation Compose** with authentication flow

### MVVM + Repository Pattern
```
UI Layer (Screens/Composables)
    ↓
ViewModels (AuthViewModel, SessionViewModel, StatisticsViewModel, etc.)
    ↓
Repositories (AuthRepository, UserRepository, SessionRepository, etc.)
    ↓
Data Sources: Room Database + Firebase Firestore
```

### Package Structure (`com.releaf.app`)
```
├── data/
│   ├── Technique.kt           # 18 relaxation techniques with metadata
│   ├── user/                  # User, Session models
│   ├── model/                 # Recommendation, UserProgress, Firebase models
│   ├── database/              # Room: AppDatabase, DAOs, Converters
│   └── repository/            # Local + Firebase repositories
├── ui/
│   ├── screens/               # 34+ screens (auth, exercises, stats)
│   ├── components/            # Reusable Compose components
│   ├── viewmodel/             # ViewModels for state management
│   ├── navigation/            # CalmNavigation.kt, BottomNavigation.kt
│   └── theme/                 # Material 3 theming
├── services/                  # StatisticsService, ProgressService, RecommendationService
├── audio/                     # SoundTherapyEngine
└── physics/                   # StressBallPhysics (Box2D)
```

### Navigation Flow
```
LoginScreen → RegisterScreen/ForgotPasswordScreen → HomeScreen
                                                        ↓
HomeScreen (technique grid) → TechniqueDetailScreen → Exercise Screens → SessionCompletionScreen
    ↓
BottomNavigation: Home | Favorites | Sleep | Profile | Statistics
```

### Room Database Entities
- `User` - Profile, preferences, stats (streak, total sessions)
- `UserPreferences` - Theme, language, notifications
- `Session` - Technique usage with mood before/after
- `TechniqueStats` - Per-technique aggregated metrics
- `UserGoal` - Daily/weekly/streak goals

## Key Implementation Details

### Adding a New Technique
1. Add technique to `TechniquesRepository.kt` (in the techniques list)
2. Create exercise screen in `ui/screens/`
3. Add route in `CalmNavigation.kt`
4. Add icon to `res/drawable/`

### Localization
- Base language: French (`res/values/strings.xml`)
- Translations: `values-en/`, `values-es/`, `values-zh-rCN/`
- **Known issue**: Many strings hardcoded in Kotlin files (see `LOCALIZATION.md`)

### Firebase Configuration
- Requires `google-services.json` in `app/`
- Demo mode available via `DemoAuthRepository` for offline development
- Collections: `sessions`, `progress`, `weeklyProgress`, `userPreferences`, `badges`, `favorites`

## Known Issues (from AUDIT.md)

### Critical
- **SoundTherapyEngine**: `startFrequency()` blocks the calling coroutine, freezing UI
- **Firestore Sessions**: Sessions created with `add()` but ID never stored; updates fail

### Major
- **Streak Calculation**: Counts sessions without checking day continuity
- **Language Change**: `applyLanguage()` doesn't restart activity; UI keeps old language
- **Room Unencrypted**: Health data stored in plaintext
- **No Room Migrations**: Schema changes will break existing installations

## Configuration

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Compose Compiler**: 1.5.4 (experimental APIs enabled)
- **Namespace**: `com.releaf.app`
