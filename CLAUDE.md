# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is "Brythee" - a native Android stress and anxiety management application ported from a Nuxt.js web version. It provides 9 relaxation techniques with interactive animations and exercises.

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
- **Kotlin** with Jetpack Compose for modern Android UI
- **Navigation Compose** for declarative navigation
- **Material 3** design system with custom theming
- **Coroutines** for asynchronous operations
- **Kotlinx Serialization** for data serialization

### Key Architecture Patterns
- **Repository Pattern**: `TechniquesRepository` serves as single source of truth for technique data
- **Compose Navigation**: Type-safe navigation with route parameters
- **State Management**: Compose state handling with `LaunchedEffect` for timers
- **Material Design**: Comprehensive Material 3 theming in `ui/theme/`

### Core Components

#### Data Layer (`data/`)
- `Technique.kt`: Data models and repository for the 9 relaxation techniques
- `TechniqueTag`: Enum for categorizing techniques by anxiety level and duration
- `TechniquesRepository`: Provides techniques, related suggestions, and tag-based filtering

#### UI Layer (`ui/`)
- **Navigation**: `CalmNavigation.kt` defines app flow (Home → Detail → Exercise)
- **Screens**: Dedicated screens for home grid, technique details, and specialized exercise types
- **Components**: Reusable `TechniqueCard` and `BreathingAnimation` components
- **Theme**: Material 3 implementation with custom colors and typography

### Navigation Flow
```
HomeScreen (grid of 9 techniques)
    ↓ (tap technique card)
TechniqueDetailScreen (description + related techniques)
    ↓ (start exercise)
ExerciseScreen / BreathingExerciseScreen / GuidedBreathingExerciseScreen
```

### Specialized Exercise Screens
- `BreathingExerciseScreen`: Interactive breathing animation with real-time visual guidance
- `GuidedBreathingExerciseScreen`: Structured breathing patterns with timing
- `ExerciseScreen`: Generic exercise template for other techniques

### Technique System
- **9 Techniques**: From 1-minute stress ball to 30-minute sound therapy
- **Tag-based Organization**: Categorized by anxiety level (high/moderate) and duration (short/medium/long)
- **Related Suggestions**: Algorithm recommends techniques based on shared tags

## Development Notes

- **Minimum SDK**: Android 24 (Android 7.0)
- **Target SDK**: 36 (latest Android)
- **Java Version**: 11 (required for modern Android development)
- Enables several experimental Compose APIs for animations and Material 3

## Testing Structure
- Unit tests: `src/test/` using JUnit
- Instrumented tests: `src/androidTest/` using Espresso and Compose testing
- Test runner: `androidx.test.runner.AndroidJUnitRunner`