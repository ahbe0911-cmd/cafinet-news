# Cafinet News (اخبار کافی‌نت)

اسکلت حرفه‌ای یک اپلیکیشن اندروید خبری با ظاهر شبیه فیدهای شبکه‌های اجتماعی
(Instagram-like)، آماده برای توسعه و اتصال به بک‌اند خبری آینده (اخبار
دریافتی از کانال‌های تلگرام).

## تکنولوژی‌ها

| لایه | تکنولوژی |
|---|---|
| زبان | Kotlin |
| UI | Jetpack Compose + Material 3 |
| معماری | Clean Architecture + MVVM |
| DI | Hilt |
| شبکه | Retrofit + OkHttp + kotlinx.serialization |
| دیتابیس محلی | Room |
| تنظیمات کاربر | DataStore Preferences |
| ناوبری | Navigation Compose |
| کارهای پس‌زمینه | WorkManager (+ Hilt Worker) |
| تصویر | Coil |
| ویدئو | Media3 ExoPlayer |
| Async | Kotlin Coroutines + Flow |

`minSdk = 24` / `targetSdk = 35`

## معماری ماژول‌ها (Multi-Module)

```
CafinetNews/
├── app                     # UI (Compose screens), navigation, DI wiring, WorkManager
├── core
│   ├── common              # Result wrapper, DispatcherProvider
│   ├── network             # Retrofit/OkHttp setup (no secrets committed)
│   ├── database             # DataStore-backed user preferences
│   └── ui                  # Theme (Color/Type/Shape), shared composables (NewsCard...)
├── domain
│   ├── model                # News, NewsCategory (pure Kotlin)
│   ├── repository          # NewsRepository interface
│   └── usecase              # GetNewsFeed/Refresh/Search/Detail/Categories use cases
└── data
    ├── api                  # Retrofit service, DTOs, Mock data source (active today)
    ├── local                # Room entities/DAO/AppDatabase
    └── repository            # NewsRepositoryImpl (offline-first, mock↔real swap point)
```

جهت وابستگی همیشه به سمت `domain` است: `app` و `data` به `domain` وابسته‌اند،
اما `domain` به هیچ فریم‌ورک اندرویدی (Room/Retrofit/Compose) وابسته نیست.

## اتصال به بک‌اند واقعی (آینده)

در حال حاضر داده‌ها از `MockNewsDataSource` (در `data:api`) خوانده می‌شوند.
برای اتصال به بک‌اند واقعی:

1. مقدار `BASE_URL` را در `core/network/build.gradle.kts` با آدرس واقعی جایگزین کنید.
2. در `data/api/src/main/java/.../ApiModule.kt`، خط زیر را:
   ```kotlin
   fun provideNewsRemoteDataSource(mock: MockNewsDataSource): NewsRemoteDataSource = mock
   ```
   به نمونه‌ی `RemoteNewsDataSource` (که از `NewsApiService` واقعی استفاده می‌کند) تغییر دهید.

هیچ کد دیگری (ViewModel، UseCase، Repository) نیاز به تغییر ندارد.

### قرارداد API فعلی (Mock)

```
GET /api/news

[
  {
    "id": 1,
    "title": "خبر نمونه",
    "description": "متن خبر",
    "imageUrl": "",
    "videoUrl": "",
    "category": "فناوری",
    "source": "کافی‌نت",
    "publishedAt": "1405/06/25",
    "viewCount": 0
  }
]
```

## مدل داده (News)

```kotlin
data class News(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String?,
    val category: String,
    val source: String,
    val publishedAt: String,
    val viewCount: Int = 0,
)
```

## صفحات

- **Splash** — نمایش برند و انتقال خودکار به Home
- **Home** — فید عمودی اخبار (LazyColumn + Coil)، فیلتر دسته‌بندی، جستجو
- **News Detail** — تصویر/ویدئوی کامل (ExoPlayer)، متن کامل، اشتراک‌گذاری
- **Settings** — انتخاب فونت (وزیرمتن/نازنین/تیتر)، اندازه متن (کوچک/متوسط/بزرگ)، پوسته (روشن/تیره/سیستم)

تنظیمات در `DataStore` ذخیره و در سراسر برنامه (از طریق `CafinetNewsRoot`) اعمال می‌شوند.

## فونت فارسی (Vazirmatn)

فایل‌های واقعی `.ttf` به‌دلیل حجم/مجوز در این اسکلت قرار داده نشده‌اند. راهنمای
افزودن آن‌ها در `core/ui/src/main/res/font/README.md` موجود است؛ تا افزودن
فونت‌ها، برنامه با فونت پیش‌فرض سیستم بدون خطا اجرا می‌شود.

## امنیت

- هیچ API Key یا Secret در کد کامیت نشده است؛ `BASE_URL` از `BuildConfig` خوانده می‌شود.
- `.gitignore` شامل `local.properties`، فایل‌های Keystore و `secrets.properties` است.
- CI (`.github/workflows/android-ci.yml`) هیچ Secret‌ای reference نمی‌کند؛ برای
  Release/Signing باید Job جداگانه با GitHub Encrypted Secrets اضافه شود.

## اجرا

```bash
git clone <repo-url>
cd CafinetNews
./gradlew assembleDebug
```

> نکته: پوشه `gradle/wrapper` شامل `gradle-wrapper.properties` است؛ در صورت
> نبود `gradle-wrapper.jar` (باینری که در این اسکلت قرار داده نشده)، یک بار
> در Android Studio پروژه را باز کنید یا دستور `gradle wrapper` را اجرا کنید
> تا Wrapper کامل تولید شود.

## گام‌های بعدی پیشنهادی

- افزودن فایل‌های فونت واقعی (Vazirmatn/Nazanin/Titr)
- اتصال `ApiModule` به بک‌اند واقعی و حذف `MockNewsDataSource`
- افزودن صفحه Full-Screen برای پخش ویدئو (در حال حاضر پخش inline پشتیبانی می‌شود)
- افزودن تست‌های واحد برای UseCase/Repository (ساختار ماژول‌ها برای تست آماده است)
- افزودن Pagination به فید (Paging 3) هنگام رشد حجم اخبار
