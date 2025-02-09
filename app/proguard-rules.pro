# Keep Firebase Authentication and Firestore classes
-keep class com.google.firebase.** { *; }
-keep class com.firebase.ui.auth.** { *; }
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.firebase.firestore.** { *; }

# Keep Jetpack Compose, ViewModel, and Activity classes
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.activity.** { *; }

# Keep custom ViewModel classes
-keep class com.example.to_dolist.viewmodel.** { *; }

# Keep Room Database entities and DAOs
-keep class androidx.room.** { *; }

# Keep classes for WebView JS interface (if you're using WebView)
# -keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
# }

# Keep line number and source file information for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep Coil classes for image loading
-keep class coil.** { *; }

# Don't remove classes/methods used by reflection or dynamically loaded
-dontwarn androidx.lifecycle.**
-dontwarn androidx.room.**
-dontwarn com.google.firebase.**
-dontwarn coil.**
