# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Contains the data models
-keep class com.busytrack.openlivetriviainterface.** { *; }

# Coroutines
-dontwarn kotlinx.coroutines.**

# OkHttp
-dontwarn com.squareup.okhttp.**

# Material Design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# Android Architecture Components
-keep class androidx.lifecycle.** {*;}

# Firebase Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep public class * extends java.lang.Exception

# Other attributes
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable