// Kelas untuk menangani data pesanan kopi
class Order {
  // Konstruktor untuk menyimpan data pesanan
  constructor(nama, email, kopi, jumlah) {
    this.nama = nama;
    this.email = email;
    this.kopi = kopi;
    this.jumlah = jumlah;
  }

  // Method untuk menampilkan alert konfirmasi kepada pengguna
  konfirmasi() {
    alert(`Terima kasih ${this.nama}, pesanan Anda untuk ${this.jumlah} ${this.kopi} telah diterima!`);
  }

  // Method untuk mencetak detail pesanan ke konsol
  cetak() {
    console.log(`Pesanan dari ${this.nama} (${this.email}): ${this.jumlah} ${this.kopi}`);
  }
}

// Fungsi untuk menangani pengiriman form order
function kirimOrder(event) {
  // Mencegah form melakukan submit default (reload halaman)
  event.preventDefault();

  // Mengambil elemen form dari event
  const form = event.target;

  // Menyusun data form dalam format URL-encoded
  const formData = new URLSearchParams();
  formData.append("nama", form.nama.value);     // Ambil nilai input nama
  formData.append("email", form.email.value);   // Ambil nilai input email
  formData.append("kopi", form.kopi.value);     // Ambil nilai select kopi
  formData.append("jumlah", form.jumlah.value); // Ambil nilai input jumlah

  // Kirim data ke server melalui fetch dengan method POST
  fetch("/submit", {
    method: "POST",
    body: formData,  // Data dikirim dalam body
    headers: {
      "Content-Type": "application/x-www-form-urlencoded", // Format data yang dikirim
    }
  })
  // Menangani response dari server
  .then(response => response.text()) // Ubah response ke teks
  .then(result => {
    alert(result);   // Tampilkan pesan dari server
    form.reset();    // Reset form setelah submit berhasil
  })
  .catch(error => {
    // Tampilkan pesan error jika gagal kirim
    alert("Gagal mengirim pesanan: " + error);
  });
}

// Fungsi dijalankan saat seluruh dokumen HTML sudah dimuat
document.addEventListener("DOMContentLoaded", function () {
  // Ambil form berdasarkan ID
  const contactForm = document.getElementById('contactForm');

  // Tambahkan event listener submit ke form
  contactForm.addEventListener('submit', kirimOrder);

  // Menangani tombol order (misalnya tombol "Pesan Sekarang")
  const orderButtons = document.querySelectorAll(".order-btn"); // Ambil semua tombol order

  // Ambil form order dan elemen select kopi
  const orderForm = document.querySelector("#Order form"); // Pastikan selector ini sesuai dengan HTML
  const kopiSelect = orderForm.querySelector("select[name='kopi']"); // Ambil dropdown kopi

  // Untuk setiap tombol order
  orderButtons.forEach(function (button) {
    // Tambahkan event listener klik
    button.addEventListener("click", function () {
      // Ambil data-kopi dari tombol yang diklik
      const kopi = this.getAttribute("data-kopi");

      // Set nilai dropdown kopi sesuai data-kopi dari tombol
      kopiSelect.value = kopi;

      // Scroll halus ke form order
      document.querySelector("#Order").scrollIntoView({
        behavior: "smooth"
      });
    });
  });
});
