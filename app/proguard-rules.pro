# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\ANDOCS/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-obfuscationdictionary dictionary-elder.txt
-keepattributes SourceFile,LineNumberTable
-keep class com.aviraxp.xpblocker.hook.HookLoader
-keep class android.** { *; }
-keepclassmembernames class com.aviraxp.xpblocker.ui.SettingsActivity {
    static boolean isActivated;
}
