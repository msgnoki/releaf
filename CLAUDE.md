# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is "Releaf" - a comprehensive native Android stress and anxiety management application. Originally ported from a Nuxt.js web version, it now features extensive user management, database persistence, Firebase integration, and 22+ specialized relaxation techniques with interactive animations and exercises.

## Build and Development Commands

```bash
# Build the project (from project root)
./gradlew assembleDebug

# Install debug build to connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build artifacts
./gradlew clean

# Build release version
./gradlew assembleRelease
```

## Project Architecture

### Tech Stack
- **Kotlin** (Java 17 target) with Jetpack Compose for modern Android UI
- **Navigation Compose** for declarative navigation with authentication flow
- **Material 3** design system with comprehensive theming and icons
- **Room Database** for local data persistence with Firebase sync
- **Firebase** (Auth, Firestore, Storage, Analytics) for cloud services
- **Coroutines & Flows** for asynchronous operations and reactive programming
- **Kotlinx Serialization** for data serialization
- **Box2D Physics Engine** for interactive stress ball mechanics
- **DataStore** for preferences management
- **WorkManager** for background tasks
- **Biometric Authentication** support

### Architecture Patterns

#### MVVM with Repository Pattern
- **ViewModels**: `AuthViewModel`, `SoundTherapyViewModel` manage UI state
- **Repositories**: `TechniquesRepository`, `AuthRepository`, `UserRepository` handle data operations
- **Database Layer**: Room entities with DAOs for local storage
- **Services**: `ProgressService`, `RoutineService`, `RecommendationService` for business logic

#### Navigation Architecture
```
Authentication Flow (if enabled):
LoginScreen → RegisterScreen / ForgotPasswordScreen → HomeScreen

Main App Flow:
HomeScreen (techniques grid + bottom navigation)
    ↓ (technique selection)
TechniqueDetailScreen (description + related techniques)
    ↓ (start exercise)
Specialized Exercise Screens
    ↓ (guided breathing only)
GuidedBreathingSelectionScreen → GuidedBreathingAdvancedScreen
```

### Data Layer Architecture

#### Core Models (`data/`)
- `Technique.kt`: 22+ relaxation techniques with categories, tags, difficulty levels
- `User.kt`, `Session.kt`: User management with session tracking
- `UserProgress.kt`: Progress tracking and statistics
- `Recommendation.kt`: AI-driven technique recommendations
- `Citation.kt`: Scientific references for techniques

#### Database Schema (`data/database/`)
- **AppDatabase**: Room database with 5 entities
- **DAOs**: Type-safe database access for users, sessions, stats, goals
- **Converters**: Custom type converters for complex data types
- **Migration Strategy**: Version control for database schema changes

#### Repository Layer
- **TechniquesRepository**: 22+ techniques with advanced filtering, categorization, and recommendation engine
- **AuthRepository**: Authentication with Firebase Auth integration
- **UserRepository**: User data management with local/cloud sync
- **CitationsRepository**: Scientific backing for techniques

### UI Layer Architecture

#### Screen Organization (`ui/screens/`)
- **Authentication**: `LoginScreen`, `RegisterScreen`, `ForgotPasswordScreen`
- **Main Navigation**: `HomeScreen`, `ProfileScreen`, `SleepScreen`
- **Technique Details**: `TechniqueDetailScreen` with related suggestions
- **Specialized Exercises**: 22+ dedicated exercise screens for different techniques
- **Advanced Features**: Multi-pattern guided breathing with selection and advanced screens

#### Component System (`ui/components/`)
- **TechniqueCard**: Reusable cards with Material 3 design
- **BreathingAnimation**: Real-time animated breathing guides
- **RoutineCard**: Daily routine management
- **SearchBar**: Technique filtering and search
- **CategoryChip**: Tag-based filtering system

#### Navigation System (`ui/navigation/`)
- **CalmNavigation**: Complete app navigation with authentication flow
- **BottomNavigation**: Tab-based navigation between main sections
- **Deep Linking**: Support for technique-specific navigation

### Advanced Features

#### Technique System
- **22+ Techniques**: Comprehensive library from quick stress relief to deep meditation
- **Categorization**: By anxiety level, duration, difficulty, and therapeutic approach
- **Recommendation Engine**: AI-driven suggestions based on usage patterns, preferences, and current state
- **Crisis Mode**: Immediate access to techniques for acute anxiety
- **Progress Tracking**: Session history, completion rates, and effectiveness metrics

#### Authentication & User Management
- **Firebase Authentication**: Email/password with forgot password functionality
- **User Profiles**: Customizable profiles with anxiety levels and preferences
- **Biometric Support**: Fingerprint/face unlock for secure access
- **Demo Mode**: Offline functionality for testing

#### Physics & Interactive Elements
- **Stress Ball**: Box2D physics engine for realistic stress ball interaction
- **Bubble Therapy**: Interactive bubble-popping for stress relief
- **Sound Therapy**: Advanced audio engine with multiple frequency options

## Development Guidelines

### Configuration Requirements
- **Minimum SDK**: Android 24 (Android 7.0) 
- **Target SDK**: 36 (latest Android)
- **Java Version**: 17 (updated from 11 for modern Android development)
- **Kotlin Compiler**: Optimized with experimental APIs enabled

### Code Conventions
- **Package Structure**: `com.example.myapplication.*` (legacy namespace)
- **Compose OptIns**: Several experimental APIs enabled for Material 3 and animations
- **Coroutines**: Extensive use of coroutines for async operations
- **State Management**: Compose state with `LaunchedEffect` for timers and lifecycle management

### Testing Structure
- **Unit Tests**: `src/test/` using JUnit 4
- **Instrumented Tests**: `src/androidTest/` using Espresso and Compose testing
- **Test Runner**: `androidx.test.runner.AndroidJUnitRunner`

### Firebase Configuration
- Firebase services must be configured with google-services.json
- Features include Auth, Firestore, Storage, and Analytics
- Demo auth repository available for offline development

### Performance Considerations
- **Build Optimizations**: ProGuard enabled for release builds
- **Resource Management**: Efficient image loading with Coil
- **Memory Management**: Proper lifecycle handling for animations and timers