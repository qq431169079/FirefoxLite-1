
# We do not want to obfuscate - It's just painful to debug without the right mapping file.
-dontobfuscate


##### Default proguard settings:

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sebastian/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# The Buddybuild SDK builds using ACRA 4.6.4, which is afflictted
# by the following bug: https://github.com/ACRA/acra/issues/301
# That is fixed in ACRA 4.7, but we need to wait for Buddybuild to upgrade
# for that to be fixed. (Note: this only affects BuddyBuild builds where
# the BuddyBuild SDK is enabled, and is not visible in local builds. You can
# enable the BuddyBuild SDK per branch for testing, by default it is only
# used for master.)
-dontwarn org.acra.ErrorReporter

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


####################################################################################################
# Adjust
####################################################################################################

-keep public class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep class dalvik.system.VMRuntime {
    java.lang.String getRuntime();
}
-keep class android.os.Build {
    java.lang.String[] SUPPORTED_ABIS;
    java.lang.String CPU_ABI;
}
-keep class android.content.res.Configuration {
    android.os.LocaledList getLocales();
    java.util.Locale locale;
}
-keep class android.os.LocaledList {
    java.util.Locale get(int);
}


### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

### greenDAO 2
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# Customized BottomSheetBehavior for ViewPager
-keep class org.mozilla.rocket.widget.ViewPagerBottomSheetBehavior

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


# For Fragments which be created by xml of Android Navigation Architecture Components
-keep public class org.mozilla.rocket.** extends androidx.fragment.app.Fragment


# Integrate buddybuild leakcanary
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# Marshmallow removed Notification.setLatestEventInfo()
-dontwarn android.app.Notification


## Integrate Glide source code
-dontwarn android.graphics.Bitmap$Config
-dontwarn android.app.FragmentManager

# kotlinx.coroutines
-dontwarn kotlinx.atomicfu.AtomicBoolean