# Add project specific ProGuard rules here.
# Keep Room, Retrofit and kotlinx.serialization models from being stripped.
-keep class com.cafinet.news.domain.model.** { *; }
-keep class com.cafinet.news.data.api.dto.** { *; }
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
