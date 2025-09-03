# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Performance optimizations for Calm app
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Keep line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep Compose annotations
-keep @androidx.compose.runtime.Stable class * { *; }
-keep @androidx.compose.runtime.Immutable class * { *; }

# Keep Technique data class for serialization
-keep class com.example.myapplication.data.Technique { *; }
-keep class com.example.myapplication.data.TechniqueTag { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Compose Navigation
-keep class androidx.navigation.compose.** { *; }

# Material Icons
-keep class androidx.compose.material.icons.** { *; }

# Aggressive optimizations for smaller APK
-allowaccessmodification
-mergeinterfacesaggressively