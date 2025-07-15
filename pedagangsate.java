import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class pedagangsate {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int hargaPerTusuk = 950;

            System.out.println("=== Program Hitung Harga Sate ===");
            System.out.print("Masukkan jumlah tusuk sate yang dipesan: ");
            int jumlahTusuk = scanner.nextInt();

            int totalHarga = hargaPerTusuk * jumlahTusuk;

            NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            System.out.println("\n=== Struk Pembelian ===");
            System.out.println("Jumlah tusuk    : " + jumlahTusuk);
            System.out.println("Harga per tusuk : " + rupiah.format(hargaPerTusuk));
            System.out.println("Total harga     : " + rupiah.format(totalHarga));
        }
    }

    @Override
    public String toString() {
        return "PedagangSateInput []";
    }
}
