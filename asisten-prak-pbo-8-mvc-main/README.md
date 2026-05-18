# MVC

Pada pertemuan ke-8 kali ini, kita akan belajar mengenai MVC. MVC (Singkatan dari Model-View-Controller) adalah pola arsitektur untuk membangun aplikasi, dengan memisahkan kode menjadi 3 bagian, yaitu:

- Model: Menyimpan dan mengelola data aplikasi.
- View: Menampilkan data ke pengguna.
- Controller: Menghubungkan model dan view, menangani interaksi pengguna.

Program ini merupakan contoh implementasi dari arsitektur MVC. Kita akan mengubah struktur program kita pada pertemuan kemarin menjadi MVC. Berikut adalah struktur folder program kita setelah diubah menjadi MVC.

![Struktur Program](gambar/struktur.jpg)

Seperti yang dapat dilihat, kita memisahkan antara Model, View, dan Controller menjadi ketiga package yang terpisah. Hal tersebut nantinya akan memudahkan kita dalam _mengoding_ program kita ketika sudah program tersebut sudah memiliki banyak fitur.

## How to setup

1. Masukkan library bernama `mysql-connector-j-8.3.0.jar` yang terdapat pada folder "TOOLS YANG DIBUTUHKAN".
2. Untuk memasukkan library, temen-temen dapat melakukan klik kanan pada bagian library, kemudian pilih Add JAR/Folder. Perhatikan gambar berikut.

![Langkah_2](gambar/tutorial_1.png)

3. Setelah itu, carilah folder bernama "TOOLS YANG DIBUTUHKAN", lalu pilih file bernama `mysql-connector-j-8.3.0.jar`. Pada bagian kanan, pastikan temen-temen memilih yang "Relative Path". Setelah itu, klik "Open". Perhatikan gambar berikut.

![Langkah_3](gambar/tutorial_2.png)

4. Nyalakan xampp, buatlah sebuah database bernama `upnvy`.
5. Import file bernama `upnvy.sql` yang terdapat pada folder "TOOLS YANG DIBUTUHKAN".
6. Jalankan program.
7. Analisislah kode yang terdapat pada program.
