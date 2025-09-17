# Repository Guidelines

## Project Structure & Module Organization
The Android app lives in a single Gradle module `app`. Kotlin sources sit in `app/src/main/java/com/releaf/app`, separated into `ui`, `data`, `services`, `audio`, `physics`, and `utils`. Jetpack Compose screens and navigation live in `ui`, while assets and string resources are in `app/src/main/assets` and `app/src/main/res`. Unit tests belong in `app/src/test/java`, mirroring the main package so shared utilities remain easy to locate. Large binary assets (icons, audio) stay under `Logo/` and `Techniques icons/`; reference them through the resource system rather than loading raw files.

## Build, Test, and Development Commands
Use Gradle via the wrapper for consistent toolchains. `./gradlew assembleDebug` compiles a debuggable APK. `./gradlew testDebugUnitTest` runs JVM unit tests; prefer it for quick checks before committing. `./gradlew lint` executes Android's static lint rules, and `./gradlew clean` resets the build cache when IDE sync fails. From Android Studio, the standard Run configuration maps to `installDebug`.

## Coding Style & Naming Conventions
Write Kotlin following Jetpack Compose idioms: four-space indentation, trailing commas for multiline argument lists, and nullable types only when required. Classes and composables use UpperCamelCase (`CalmNavigation`), functions and properties use lowerCamelCase, and constants live in `object` holders with SCREAMING_SNAKE_CASE. Keep composables short and stateless; move calculations into view model or service layers. Run Android Studio's `Code > Reformat` (or `⌥⌘L`/`Ctrl+Alt+L`) before committing to normalise imports and spacing.

## Testing Guidelines
Unit tests use JUnit (`app/src/test/java`). Name files after the class under test, ending in `Test` (e.g., `ProgressServiceTest`). Mock external dependencies with lightweight fakes instead of live Firebase calls. Run `./gradlew testDebugUnitTest` locally and ensure new features include at least one test covering the primary scenario and an error path.

## Commit & Pull Request Guidelines
Commit messages follow a `type: succinct summary` style (`feat: Add routine scheduler`). Group unrelated changes into separate commits. Pull requests should highlight intent, list user-visible impacts, link tracking issues, and include screenshots or screen recordings for UI updates. Verify lint and tests before opening a PR to keep CI green.

## Configuration Tips
Keep Firebase credentials in the checked-in `google-services.json` and avoid embedding secrets in code. Use `local.properties` for machine-specific paths. When adding new services, document required environment steps in `README.md` and revise this guide if workflows change.
