<div align="center">
  <a href="README.md">
    <img src="https://img.shields.io/badge/Language-English-blue?style=flat" alt="English">
  </a>
</div>

<div align="center">
  <h1>Cinevo</h1>
  
  <p>
    <strong>Modern Mobil Film Keşif Uygulaması</strong>
  </p>

  <p>
    <a href="https://kotlinlang.org/">
      <img src="https://img.shields.io/badge/Kotlin-1.9.0-purple?style=flat&logo=kotlin" alt="Kotlin">
    </a>
    <a href="https://developer.android.com/jetpack/compose">
      <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=flat&logo=android" alt="Jetpack Compose">
    </a>
    <a href="https://firebase.google.com/">
      <img src="https://img.shields.io/badge/Firebase-Backend-FFCA28?style=flat&logo=firebase" alt="Firebase">
    </a>
    <a href="https://www.themoviedb.org/">
      <img src="https://img.shields.io/badge/TMDb-API-01B4E4?style=flat&logo=themoviedb" alt="TMDb">
    </a>
  </p>
</div>

<br />

## Proje Hakkında

Cinevo, film tutkunlarının yeni yapımları keşfetmesini, detaylı bilgilere ulaşmasını ve favori listelerini oluşturmasını sağlayan modern bir Android uygulamasıdır.

Jetpack Compose ile tamamen deklaratif bir arayüz (UI) üzerine inşa edilmiş olup, Clean Architecture prensiplerine sadık kalınarak geliştirilmiştir. Veri akışı için TMDb API kullanılırken, kullanıcı özelleştirmeleri ve favori listeleri Firebase Firestore üzerinde güvenli bir şekilde saklanır.

---

## Özellikler

* **Gelişmiş Film Keşfi:** Popüler, vizyondaki ve trend filmleri anlık olarak görüntüleyin.
* **Akıllı Arama:** Film adı ile hızlı ve dinamik arama yapın.
* **Detaylı Görünüm:** Film afişi, özet, IMDb puanı, yayın tarihi ve süre bilgileri.
* **Fragman İzleme:** YouTube entegrasyonu (Intents) ile fragmanları doğrudan izleyin.
* **Favoriler:** Beğendiğiniz filmleri Firebase Firestore altyapısı ile buluta kaydedin ve her yerden erişin.

---

## Ekran Görüntüleri

<div align="center">
  <table>
    <tr>
      <td align="center"><b>Ana Sayfa</b></td>
      <td align="center"><b>Detay Ekranı</b></td>
      <td align="center"><b>Arama</b></td>
      <td align="center"><b>Favoriler</b></td>
    </tr>
    <tr>
      <td><img src="./screenshots/home.png" width="200" /></td>
      <td><img src="./screenshots/details.png" width="200" /></td>
      <td><img src="./screenshots/search.png" width="200" /></td>
      <td><img src="./screenshots/favorites.png" width="200" /></td>
    </tr>
  </table>
</div>

---

## Kullanılan Teknolojiler ve Kütüphaneler

Bu proje, modern Android geliştirme ekosisteminin en güncel araçları kullanılarak geliştirilmiştir:

* **Dil:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Mimari:** MVVM (Model-View-ViewModel) & Clean Architecture
* **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
* **Ağ İşlemleri (Network):** [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson)
* **Eşzamanlılık (Concurrency):** [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) & Flow
* **Görsel Yükleme:** [Coil](https://coil-kt.github.io/coil/)
* **Backend & Veritabanı:** * Firebase Authentication
    * Firebase Firestore
* **API:** [The Movie Database (TMDb) API](https://www.themoviedb.org/documentation/api)

---

## Kurulum

Projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları izleyin:

1.  **Repoyu Klonlayın:**
    ```bash
    git clone [https://github.com/ArmaganOzkan/cinevo.git](https://github.com/ArmaganOzkan/cinevo.git)
    ```

2.  **API Anahtarı (Key) Yapılandırması:**
    * [TMDb](https://www.themoviedb.org/) üzerinden ücretsiz bir API anahtarı alın.
    * `local.properties` dosyasına şu satırı ekleyin:
        ```properties
        TMDB_API_KEY="Your_Api_Key"
        ```

3.  **Firebase Kurulumu:**
    * Firebase konsolunda yeni bir proje oluşturun.
    * `google-services.json` dosyasını indirip `app/` klasörünün içine yerleştirin.

4.  **Derleme (Build):**
    * Android Studio'da projeyi açın ve "Sync Project with Gradle Files" butonuna tıklayın.
    * Emülatör veya fiziksel cihazda çalıştırın.

---

## Geliştirici

<a href="https://github.com/ArmaganOzkan">
  <img src="https://img.shields.io/badge/GitHub-Profilim-black?style=flat&logo=github" alt="GitHub Profile">
</a>
