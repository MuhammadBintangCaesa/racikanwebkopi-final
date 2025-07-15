// Import library HTTP server bawaan Java
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        // Membuat HTTP server di port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        // Tambahkan handler untuk gambar di /images
        server.createContext("/images", new ImageHandler());

        // Menambahkan route "/" untuk menampilkan form HTML
        server.createContext("/", new FormHandler());

        // Menambahkan route "/test" untuk tes endpoint
        server.createContext("/test", new ApiTest());

        // Menambahkan route "/submit" untuk menerima data pesanan dari form
        server.createContext("/submit", new OrderHandler());

        // Menambahkan route "/racikancoffe1.css" untuk melayani file CSS
        server.createContext("/racikancoffe1.css", new CssHandler());
        server.createContext("/pembayaran", new PaymentHandler());

        // Menentukan executor (null artinya pakai default executor)
        server.setExecutor(null);
        
        // Menampilkan pesan bahwa server sudah berjalan
        System.out.println("Server running on http://localhost:8080");
        
        // Menjalankan server
        server.start();
    }
    // Handler untuk melayani gambar-gambar
// Handler untuk melayani gambar-gambar dengan ekstensi berbeda
static class ImageHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        // Mendapatkan path gambar dari URL
        String path = exchange.getRequestURI().getPath();
        // Menghilangkan '/images' untuk mendapatkan nama file
        String fileName = path.substring("/images".length());
        File file = new File("images" + fileName); // Folder images untuk menyimpan gambar

        if (file.exists()) {
            // Jika file ditemukan, baca isinya
            byte[] response = new byte[(int) file.length()];
            new FileInputStream(file).read(response);

            // Tentukan MIME type berdasarkan ekstensi file
            String mimeType = "image/jpeg"; // Default untuk JPEG
            if (fileName.endsWith(".png")) {
                mimeType = "image/png";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (fileName.endsWith(".gif")) {
                mimeType = "image/gif";
            } else if (fileName.endsWith(".bmp")) {
                mimeType = "image/bmp";
            }

            exchange.getResponseHeaders().set("Content-Type", mimeType);

            // Kirim response dengan kode 200 dan panjang file
            exchange.sendResponseHeaders(200, response.length);

            // Tulis isi file ke output stream
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        } else {
            // Jika gambar tidak ditemukan, kirim pesan error 404
            String notFound = "Image not found";
            exchange.sendResponseHeaders(404, notFound.length());
            OutputStream os = exchange.getResponseBody();
            os.write(notFound.getBytes());
            os.close();
        }
    }
}


    // Handler untuk menyajikan file CSS
    static class CssHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // Membaca file CSS
            File file = new File("racikancoffe1.css"); // File CSS harus berada di direktori yang sama

            if (file.exists()) {
                // Jika file ditemukan, baca isinya
                byte[] response = new byte[(int) file.length()];
                new FileInputStream(file).read(response);

                // Mengatur header content-type agar dikenali sebagai CSS
                exchange.getResponseHeaders().set("Content-Type", "text/css");

                // Kirim response dengan kode 200 dan panjang file
                exchange.sendResponseHeaders(200, response.length);

                // Tulis isi file ke output stream
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } else {
                // Jika file tidak ditemukan, kirim pesan error 404
                String notFound = "CSS file not found";
                exchange.sendResponseHeaders(404, notFound.length());
                OutputStream os = exchange.getResponseBody();
                os.write(notFound.getBytes());
                os.close();
            }
        }
    }

   
    // Handler dummy untuk testing endpoint "/test"
    static class ApiTest implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // Kirim response kosong dengan panjang 10 byte (isi tidak jelas karena hanya tulis byte 10)
            exchange.sendResponseHeaders(200, 10);
            OutputStream os = exchange.getResponseBody();
            os.write(10); // Ini bisa dikembangkan, sekarang hanya menulis satu byte
            os.close();
        }
    }

    // Handler untuk menampilkan form HTML
    static class FormHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // Membaca file HTML dari disk
            File file = new File("racikankopi.html");
            byte[] response = new byte[(int) file.length()];
            new FileInputStream(file).read(response);

            // Kirim file HTML ke browser
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    // Handler untuk halaman pembayaran
static class PaymentHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        File file = new File("pembayaran.html");
        byte[] response = new byte[(int) file.length()];
        new FileInputStream(file).read(response);

        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}

    // Handler untuk menangani form submission via POST ke "/submit"
    static class OrderHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // Pastikan metode request adalah POST
            if ("POST".equals(exchange.getRequestMethod())) {
                // Baca body dari POST request
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder buf = new StringBuilder();
                String line;

                // Gabungkan semua baris body jadi satu string
                while ((line = reader.readLine()) != null) {
                    buf.append(line);
                }

                // Parsing form data dari string ke Map
                Map<String, String> formData = parseFormData(buf.toString());

                // Ambil data dari form
                String nama = formData.get("nama");
                String email = formData.get("email");
                String kopi = formData.get("kopi");
                int jumlah = Integer.parseInt(formData.get("jumlah"));

                // Simpan data ke database (asumsikan DBHelper sudah dibuat)
                DBHelper.saveOrder(nama, email, kopi, jumlah);

                // Redirect ke halaman pembayaran setelah simpan pesanan
                exchange.getResponseHeaders().set("Location", "/pembayaran");
                exchange.sendResponseHeaders(302, -1); // -1 artinya tidak ada body
                exchange.close();
            }
        }

        // Fungsi untuk memparsing data form (x-www-form-urlencoded) menjadi Map
        private Map<String, String> parseFormData(String data) throws UnsupportedEncodingException {
            Map<String, String> map = new HashMap<>();

            // Pisahkan data berdasarkan '&' lalu split key=value
            for (String pair : data.split("&")) {
                String[] parts = pair.split("=");

                // Decode key dan value dari URL encoding (misal %20 jadi spasi)
                String key = URLDecoder.decode(parts[0], "UTF-8");
                String value = URLDecoder.decode(parts[1], "UTF-8");
                map.put(key, value);
            }

            return map;
        }
    }
}
