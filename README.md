# Aplikasi Biro Jasa Pajak Kendaraan

Aplikasi desktop Java Swing untuk pengelolaan data biro jasa pajak kendaraan. Project ini menggunakan NetBeans, Maven, database H2 embedded, dan komponen `JDateChooser` dari library `jcalendar`.

## Fitur Utama

- Login user aktif/nonaktif.
- Data pelanggan.
- Data kendaraan dan relasi pelanggan.
- Pembayaran/perpanjangan pajak kendaraan.
- Balik nama kendaraan.
- Monitoring proses.
- Manajemen user.
- Laporan, cetak, dan export CSV.
- Upload dokumen pendukung ke folder `uploads/` yang dibuat otomatis saat aplikasi berjalan.

## Teknologi

- Java 17 atau lebih baru.
- NetBeans 21 atau versi yang kompatibel.
- Maven.
- Dependency Maven:
  - `h2`
  - `jcalendar`

## Database

Aplikasi memakai **H2 embedded**, jadi tidak perlu install MySQL atau server database apa pun.
Saat pertama kali dibuka, aplikasi membuat file database, tabel, dan data demo otomatis di:

```text
~/.biro-jasa-akbar/biro_jasa_pajak.mv.db
```

File upload (KTP, STNK, dll) disimpan di `~/.biro-jasa-akbar/uploads/`.
Backup data cukup dengan menyalin folder `~/.biro-jasa-akbar`. Data tiap komputer terpisah.

Lokasi bisa diganti lewat `src/main/resources/db.properties` (`db.dir`), environment
variable `DB_DIR`, atau system property `-Ddb.dir=/path/folder`.

## Cara Membuka di NetBeans

1. Extract ZIP project.
2. Buka NetBeans.
3. Pilih **File > Open Project**.
4. Pilih folder project ini.
5. Klik kanan project > **Clean and Build**.
6. Klik **Run Project**.

Main class:

```text
com.kelompok2.pajakform.Main
```

## Cara Build dan Run lewat Terminal

Build:

```bash
mvn clean package
```

Run JAR hasil build:

```bash
java -jar target/biro-jasa-pajak-1.0.0.jar
```

Contoh run sambil override konfigurasi database:

```bash
java -Ddb.dir=/path/folder-data -jar target/biro-jasa-pajak-1.0.0.jar
```

## Build .exe untuk Windows

Ikon aplikasi sudah tersedia di `packaging/logo-akbar.ico` (7 ukuran: 16-256 px).
Perintah berikut dijalankan **di Windows** dengan JDK 17+ (`jpackage` sudah termasuk di JDK):

```bash
mvn clean package
```

Siapkan folder input (sekali tiap habis build):

```bash
mkdir -p build/app && cp target/biro-jasa-pajak-1.0.0.jar build/app/
```

```bash
jpackage --type app-image --name "Biro Jasa Akbar" --app-version 1.0.0 --vendor "PT Biro Jasa Akbar Berjaya" --input build/app --main-jar biro-jasa-pajak-1.0.0.jar --main-class com.kelompok2.pajakform.Main --icon packaging/logo-akbar.ico --dest dist --win-console
```

Hasilnya `dist/Biro Jasa Akbar/Biro Jasa Akbar.exe` beserta runtime Java-nya, jadi
komputer tujuan tidak perlu instal Java. Ganti `--type app-image` dengan `--type msi`
(butuh WiX Toolset) kalau mau installer. Buang `--win-console` kalau tidak ingin
jendela konsol ikut terbuka.

Catatan: `--input` harus folder yang isinya **hanya** JAR aplikasi. Folder `target/`
juga berisi `original-biro-jasa-pajak-1.0.0.jar` (versi tanpa dependency), dan jpackage
memasukkan semua JAR di folder input ke classpath, jadi keduanya bentrok.

### Ikon saat JAR dijalankan langsung

`java -jar` di macOS tidak memakai `.icns`, jadi ikon Dock-nya diset dari kode
(`Tampilan.pasangIconAplikasi()`). Nama aplikasi di menu bar tetap "java" kecuali
dijalankan dari bundel `.app`, atau ditambah flag berikut:

```bash
java -Xdock:name="Biro Jasa Akbar" -jar target/biro-jasa-pajak-1.0.0.jar
```

## Build untuk macOS

Ikon macOS ada di `packaging/logo-akbar.icns` (16-1024 px). `.dmg` **tidak wajib** —
itu cuma pembungkus untuk dibagikan. Yang dijalankan user tetap `.app` di dalamnya.

Siapkan folder input (sekali tiap habis build):

```bash
mkdir -p build/app && cp target/biro-jasa-pajak-1.0.0.jar build/app/
```

Bikin `.app` saja (paling cepat, bisa langsung dobel klik):

```bash
jpackage --type app-image --name "Biro Jasa Akbar" --app-version 1.0.0 --vendor "PT Biro Jasa Akbar Berjaya" --input build/app --main-jar biro-jasa-pajak-1.0.0.jar --main-class com.kelompok2.pajakform.Main --icon packaging/logo-akbar.icns --dest dist
```

Bikin `.dmg` untuk dibagikan (ganti `--type` saja):

```bash
jpackage --type dmg --name "Biro Jasa Akbar" --app-version 1.0.0 --vendor "PT Biro Jasa Akbar Berjaya" --input build/app --main-jar biro-jasa-pajak-1.0.0.jar --main-class com.kelompok2.pajakform.Main --icon packaging/logo-akbar.icns --dest dist
```

Pilih `--type pkg` kalau mau installer yang menaruh aplikasi ke `/Applications`.

Catatan Gatekeeper: hasil build tanpa sertifikat Apple Developer akan ditolak macOS di
komputer lain ("app is damaged" atau "cannot be opened"). Untuk pemakaian sendiri atau
demo, buka lewat klik kanan > **Open**, atau jalankan sekali:

```bash
xattr -dr com.apple.quarantine "dist/Biro Jasa Akbar.app"
```

Untuk distribusi luas butuh signing + notarization berbayar (`--mac-sign`).

## Akun Demo

Akun akan dibuat otomatis saat tabel `users` masih kosong.

| Username | Password | Status | Keterangan |
|---|---|---|---|
| `admin` | `admin` | Aktif | Administrator default |
| `admin2` | `admin2` | Aktif | Admin kedua |
| `operator1` | `op123` | Aktif | Operator satu |
| `operator2` | `op456` | Aktif | Operator dua |
| `operator3` | `op789` | Aktif | Operator tiga |
| `operator4` | `op000` | Aktif | Operator empat |
| `operator5` | `op111` | Aktif | Operator lima |
| `operator6` | `op222` | Aktif | Operator enam |
| `kasir1` | `kasir123` | Aktif | Kasir utama |
| `viewer1` | `view123` | Nonaktif | Contoh user nonaktif |

> Catatan: password demo masih plain text mengikuti struktur project awal. Untuk aplikasi nyata, gunakan hashing password seperti BCrypt.

## Perubahan Tampilan Versi Ini

Perubahan tampilan dibuat tanpa file helper Java tambahan seperti `Tampilan.java` atau `Validasi.java`. Aturan desain dan validasi ditempatkan di file JFrame/Form yang sudah ada, sehingga jumlah file Java baru tidak bertambah.

Aturan tampilan yang diterapkan:

- Ukuran window default: `1280 x 800`.
- Warna utama: navy dan gold.
- Typography:
  - `10` untuk badge/caption kecil.
  - `11` untuk label form/helper text.
  - `13` untuk body dan isi tabel.
  - `14` untuk body agak besar/deskripsi.
  - `16` untuk sub-heading/judul card.
  - `18` untuk heading section/judul panel.
  - `22` untuk judul halaman.
  - `28` untuk heading utama/dashboard.
  - `34` untuk display/branding besar.
- Text field dibuat lebih rapi dengan padding, border rounded, background putih, dan aksen gold.
- Tombol memakai warna gold dengan efek hover.
- Tabel memakai header navy, row height lebih nyaman, selection highlight, dan grid lebih halus.

## Validasi Input

Validasi yang sudah ditambahkan:

- Tahun kendaraan wajib 4 digit dan berada pada rentang wajar.
- Tanggal transaksi wajib dipilih.
- Biaya wajib angka dan tidak boleh negatif.
- No. HP wajib 10-15 digit dan diawali `08` atau `62`.
- NIK wajib tepat 16 digit angka.
- No. Polisi memakai format umum Indonesia, misalnya `B 1234 CD`.
- Hapus data membutuhkan dialog konfirmasi terlebih dahulu.

## Struktur Project Singkat

```text
src/main/java/com/kelompok2/pajakform/
├── Main.java
├── Database.java
├── FormLogin.java
├── FormMenu.java
├── FormPelanggan.java
├── FormKendaraan.java
├── FormPembayaranPajak.java
├── FormBalikNama.java
├── FormMonitoring.java
├── FormLaporan.java
├── FormUserAdmin.java
├── FileUploader.java
├── Responsive.java
├── Session.java
└── *Store.java
```

## File yang Dibersihkan

File/folder hasil build dan file runtime tidak disertakan lagi di ZIP ini, karena bisa dibuat ulang saat build/run:

```text
target/
build/
dist/
uploads/
*.class
```

Folder `uploads/` akan dibuat otomatis ketika user melakukan upload file dari aplikasi.

## Logo dan Tone Warna

Versi ini sudah menambahkan logo **PT Biro Jasa Akbar Berjaya** pada:

- `FormLogin`
- `FormMenu`
- icon window aplikasi

File logo ditempatkan di:

```text
src/main/resources/images/logo-akbar-rounded.png
src/main/resources/images/logo-akbar-icon.png
packaging/logo-akbar.ico     # ikon Windows (.exe)
packaging/logo-akbar.icns    # ikon macOS (.app/.dmg)
```

Logo dibuat dalam format PNG transparan dengan bentuk lingkaran/full rounded. Tone warna aplikasi juga disesuaikan dengan logo, memakai navy gelap, navy soft, gold, gold dark, dan background terang bernuansa biru muda.
