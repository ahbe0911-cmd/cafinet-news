# فونت‌ها

فایل‌های واقعی فونت (`.ttf`) به دلایل مجوز/حجم در این اسکلت قرار داده نشده‌اند.
برای فعال‌سازی کامل فارسی، فایل‌های زیر را در پوشه
`core/ui/src/main/res/font` اضافه کنید:

- `vazirmatn_regular.ttf`, `vazirmatn_medium.ttf`, `vazirmatn_bold.ttf`
- `nazanin_regular.ttf`
- `titr_regular.ttf` / `titr_bold.ttf`

سپس در `AppFonts.kt` مسیر `FontFamily(Font(R.font.vazirmatn_regular), ...)` را
از حالت fallback (`FontFamily.Default`) به فونت واقعی تغییر دهید. تا آن زمان
برنامه با فونت پیش‌فرض سیستم اجرا می‌شود تا بیلد بدون خطا باقی بماند.
