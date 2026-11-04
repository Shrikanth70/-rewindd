# ProGuard / R8 rules for Rewindd

# General attributes
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontwarn java.lang.invoke.**

# Google Crypto / Tink annotations & optional dependencies
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**
-dontwarn com.google.api.client.**
-dontwarn org.joda.time.**
-dontwarn org.jspecify.annotations.**

# AndroidX Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * {
    @androidx.room.TypeConverter *;
}
-keep class com.rewindd.app.core.database.** { *; }
-keep class com.rewindd.app.data.** { *; }

# Domain models & Export data models
-keep class com.rewindd.app.domain.model.** { *; }
-keep class com.rewindd.app.domain.export.** { *; }

# WorkManager
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}
-keep class androidx.work.** { *; }

# Security & Crypto
-keep class androidx.security.crypto.** { *; }
-keep class com.google.crypto.tink.** { *; }

# Jsoup HTML parsing
-keep class org.jsoup.** { *; }

# Coil Image Loading
-keep class coil.** { *; }
