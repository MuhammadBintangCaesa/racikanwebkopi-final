// Import library untuk koneksi ke database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBHelper {
    // Konstanta URL koneksi ke database MySQL lokal (db_racikankopi di port 3306)
    private static final String URL = "jdbc:mysql://localhost:3306/db_racikankopi";

    // Username database
    private static final String USER = "bintang123";

    // Password database
    private static final String PASS = "Bintang123@";

    // Method untuk menyimpan data pesanan ke database
    public static void saveOrder(String nama, String email, String kopi, int jumlah) {
        try {
            // Memuat driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuka koneksi ke database menggunakan URL, USER, dan PASS
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // Query SQL untuk menyimpan data ke tabel 'orders'
            String sql = "INSERT INTO orders (nama, email, kopi, jumlah) VALUES (?, ?, ?, ?)";

            // Menggunakan PreparedStatement untuk mengisi parameter secara aman
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);   // Isi parameter ke-1 dengan nama
            stmt.setString(2, email);  // Isi parameter ke-2 dengan email
            stmt.setString(3, kopi);   // Isi parameter ke-3 dengan jenis kopi
            stmt.setInt(4, jumlah);    // Isi parameter ke-4 dengan jumlah

            // Menjalankan perintah insert ke database
            stmt.executeUpdate();

            // Menutup statement dan koneksi
            stmt.close();
            conn.close();
        } catch (Exception e) {
            // Menampilkan error jika terjadi kesalahan
            e.printStackTrace();
        }
    }
}
