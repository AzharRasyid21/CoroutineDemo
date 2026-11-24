# Coroutine Demo — Kotlin Coroutines & Asynchronous Programming (M17)

Implementasi tugas mata kuliah **Pengembangan Aplikasi Bergerak (Modul M17: Kotlin Coroutines and Asynchronous Programming)** menggunakan arsitektur **MVVM**, **Jetpack Compose**, **StateFlow**, dan **Kotlin Coroutines**.

---

## Fitur Aplikasi & Hasil Modifikasi

### 1. Fitur Dasar Modul M17
* **Load (Konkuren):** Memuat daftar tugas (`2000 ms`), pengumuman (`1000 ms`), dan jumlah mata kuliah (`1500 ms`) secara bersamaan menggunakan `async` di dalam `coroutineScope`.
* **Load (fail):** Simulasi kegagalan koneksi server (`IOException`) yang ditangani melalui *structured concurrency* dan blok `try/catch` pada `TasksViewModel`.
* **Cancel:** Membatalkan proses pemuatan yang sedang berjalan menggunakan `Job.cancel()` tanpa memicu pesan error palsu.
* **Tap me:** Tombol penghitung (*counter*) untuk membuktikan bahwa proses *suspend* di *background thread* (`Dispatchers.IO`) tidak menghambat *main thread* (UI tetap responsif).
* **Block UI:** Simulasi pemblokiran *main thread* menggunakan `Thread.sleep(3000L)` sebagai perbandingan langsung dampak kode sinkron yang berat pada UI.

### 2. Pengembangan & Modifikasi Tambahan
* **Real-time Execution Timer:** Mengukur dan menampilkan total waktu muat secara konkuren (`measureTimeMillis`) langsung pada kartu informasi untuk membuktikan bahwa waktu muat berjalan paralel (~2000 ms, bukan akumulatif).
* **Pencarian Tugas (Search Filter):** Kolom pencarian untuk menyaring daftar tugas berdasarkan judul atau nama mata kuliah secara *real-time*.
* **Task Completion Checklist:** Fitur `Checkbox` pada setiap kartu tugas untuk menandai tugas yang sudah selesai dengan efek *strikethrough*.
* **Pengurutan Tugas (Sort A-Z):** Mengurutkan daftar tugas berdasarkan judul menggunakan `withContext(Dispatchers.Default)`.
* **Informasi Deadline Tugas:** Penambahan atribut `deadline` pada model data `Task`.

---

<img width="469" height="216" alt="Screenshot 2026-11-24 200116" src="https://github.com/user-attachments/assets/45490a3e-2bf7-4dde-8d81-fad555264da5" />
<img width="383" height="856" alt="Screenshot 2026-11-24 195850" src="https://github.com/user-attachments/assets/4edc84df-55e3-440d-acbf-b8737ce5f924" />
<img width="407" height="842" alt="Screenshot 2026-11-24 195831" src="https://github.com/user-attachments/assets/b899796f-7140-4e85-a27a-f36803e87dad" />
<img width="390" height="861" alt="Screenshot 2026-11-24 195814" src="https://github.com/user-attachments/assets/eb713b18-6752-4e78-91b1-ffb96c2fac7c" />

## Struktur Proyek

```text
com.example.coroutinedemo
├── data
│   ├── Task.kt                 # Model data tugas (id, title, course, deadline, isCompleted)
│   └── TaskRepository.kt       # Simulasi data jaringan asinkron dengan Dispatchers.IO
├── ui
│   ├── main
│   │   ├── TasksUiState.kt     # Immutable UI state & filter pencarian
│   │   ├── TasksViewModel.kt   # Logika coroutine (viewModelScope, async, withContext, Job)
│   │   └── TasksScreen.kt      # Tampilan Jetpack Compose (State Hoisting)
│   └── theme
└── MainActivity.kt             # Entry point aplikasi



