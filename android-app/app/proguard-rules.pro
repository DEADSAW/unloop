# ProGuard/R8 Rules for Unloop - Production Optimized

# Keep Room entities
-keep class com.unloop.app.data.** { *; }

# Keep accessibility service
-keep class com.unloop.app.service.UnloopAccessibilityService { *; }
-keep class com.unloop.app.service.MusicNotificationListenerService { *; }

# Remove all debug logging in release builds
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Optimize aggressively
-optimizationpasses 5
-allowaccessmodification
-repackageclasses ''

# Remove unused code
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Keep Kotlin metadata for Room
-keepattributes *Annotation*
-keepattributes Signature

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }
-keepclassmembernames class kotlinx.coroutines.internal.MainDispatcherFactory {}

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
