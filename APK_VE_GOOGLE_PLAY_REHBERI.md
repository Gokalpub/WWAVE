# WaveClean — Eksiksiz İmzalı APK ve Google Play (.aab) Paket Rehberi

Bu pakette **hiçbir eksik dosya bırakılmamıştır**:
1. **Gerçek AdMob Kimlikleriniz Gömülüdür:**
   - Uygulama Kimliği (`AndroidManifest.xml`): `ca-app-pub-8819233961749663~1707868640`
   - Ödüllü Reklam Birimi (`MainActivity.kt`): `ca-app-pub-8819233961749663/5113916136`
2. **Her İşlemde Reklam Tetiklenir (`triggerAdOnAction`):**
   - Su çıkarma başlatma/durdurma, temizlik bitişi, mod değiştirme (165Hz, 150-220Hz, 180Hz Turbo, 300Hz Toz), hoparlör hedefi seçimi, manuel frekans başlatma, dalga tipi seçimi, Sağ/Sol stereo testi, netlik testi ve sekme geçişlerinin tamamı `AndroidAdMob.triggerAdOnAction` köprüsüne bağlanmıştır. Her reklam kapandığında bir sonraki işlem için yeni reklam otomatik olarak arka planda yüklenir (`loadRewardedAd()`).
3. **Hazır İmza Anahtarı (`app/waveclean-release-key.jks`):**
   - Sizin için 10.000 gün geçerli gerçek Android Release Keystore (`waveclean-release-key.jks`, Şifre: `WaveClean2026!`, Alias: `waveclean`) üretilmiş ve `app/build.gradle.kts` içine otomatik tanımlanmıştır.
4. **Hazır Uygulama İkonları ve Mağaza Görselleri:**
   - Tüm Android çözünürlük ikonları (`mipmap-mdpi` .. `mipmap-xxxhdpi`) ve Google Play Console için `play_store_gorselleri/playstore_icon_512x512.png` ile `feature_graphic_1024x500.png` hazırdır.

## APK ve AAB Dosyasını Tek Tıkla Alma (2 Yöntem)

### Yöntem 1: Bilgisayar Olmadan Doğrudan Telefondan (GitHub Actions Bulut Derleyici)
Google Mobile Ads SDK (`com.google.android.gms:play-services-ads:25.5.0`) Google'ın Maven sunucularından derleme sırasında indirildiği için paketin içine `.github/workflows/build-apk.yml` bulut derleyicisi eklenmiştir:
1. **github.com** üzerinde ücretsiz yeni bir depo (Repository) açıp bu ZIP içindeki dosyaları yükleyin.
2. Üstteki **Actions** sekmesine tıklayın; GitHub sunucuları 2 dakika içinde projenizi `waveclean-release-key.jks` ile imzalayarak derler.
3. Tamamlanan işlemin altındaki **Artifacts** bölümünden hem **WaveClean-Imzali-APK** (`.apk`) hem de **WaveClean-GooglePlay-AAB** (`.aab`) dosyanızı doğrudan telefonunuza veya bilgisayarınıza indirin.

### Yöntem 2: Bilgisayarda Android Studio İle
1. Klasörü Android Studio ile açın.
2. İmza anahtarı zaten `app/build.gradle.kts` içinde tanımlı olduğu için yalnızca **Build > Build Bundle(s) / APK(s) > Build APK(s)** veya **Build Bundle(s)** seçeneğine tıklamanız yeterlidir.
