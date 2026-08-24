
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Gabungan1 {

    // Record untuk modul-modul
    public record Barang(String nama, int stok, int tahunBeli) {

    }

    public record Pendapatan(String tanggal, int transaksi, float total) {

    }

    public record Karyawan(String nama, String jabatan, String status, int upah) {

    }

    public record menu(String makanan, Integer harga, String minuman, Integer total) {

    }

    public record BahanMasak(String bahan, int stok, float harga) {

    }

    public static void main(String[] args) throws IOException {
        Scanner scn = new Scanner(System.in);
        boolean aplikasiJalan = true;

        while (aplikasiJalan) {
            System.out.println("\n===========================================");
            System.out.println(">|<  SISTEM MANAJEMEN RUMAH MAKAN 2026 >|<");
            System.out.println("===========================================");
            System.out.println("1. Modul Inventory Barang");
            System.out.println("2. Modul Pendapatan Harian");
            System.out.println("3. Modul Data Karyawan");
            System.out.println("4. Modul Bahan Masak");
            System.out.println("5. Modul Menu Makanan dan Minuman");
            System.out.println("6. Keluar Aplikasi");
            System.out.print("\nPilih Modul: ");

            String pilihanModul = scn.nextLine();

            switch (pilihanModul) {
                case "1" ->
                    menuInventory(scn);
                case "2" ->
                    menuPendapatan(scn);
                case "3" ->
                    menuKaryawan(scn);
                case "4" ->
                    menuBahanMasak(scn);
                case "5" ->
                    menuMenuMakanan(scn);
                case "6" -> {
                    System.out.println("Menutup sistem... Sampai jumpa!");
                    aplikasiJalan = false;
                }
                default ->
                    System.out.println("Pilihan tidak valid!");
            }

        }
        scn.close();
    }

    // ==========================================================
    // MODUL 1: INVENTORY BARANG
    // ==========================================================
    private static void menuInventory(Scanner scn) throws IOException {
        ArrayList<Barang> daftarBarang = new ArrayList<>();
        ArrayList<Barang> bacaBarang = bacaDatabase();

        boolean jalan = true;

        while (jalan) {
            System.out.println("\n================================================");
            System.out.println(">|< APLIKASI INVENTORY BARANG BY SRI WAHYUNI >|<");
            System.out.println("================================================\n");
            System.out.println("1. Tambah Barang (Create)");
            System.out.println("2. Lihat Barang (Read)");
            System.out.println("3. Ubah Barang (Update)");
            System.out.println("4. Hapus Barang (Delete)");
            System.out.println("5. Cari Barang Terlama");
            System.out.println("6. Kembali");
            System.out.print("\nPilih opsi: ");

            String pilih = scn.nextLine();

            switch (pilih) {
                case "1" -> {
                    tambahBarang(bacaBarang, scn);
                }
                case "2" ->
                    tampilkanBarang(bacaBarang);
                case "3" ->
                    ubahBarang(bacaBarang, scn);
                case "4" ->
                    hapusBarang(bacaBarang, scn);
                case "5" ->
                    cariBarangTahunTerlama(bacaBarang);
                case "6" -> {
                    simpanDatabase(bacaBarang);
                    jalan = false;

                }
                default ->
                    System.out.println("Pilihan tidak valid!");
            }
        }

    }

    private static void simpanDatabase(ArrayList<Barang> list) throws IOException {
        try (PrintWriter da = new PrintWriter(new FileWriter("DatabaseBarang.txt", false))) {
            for (Barang b : list) {
                da.println(b.nama() + "|" + b.stok() + "|" + b.tahunBeli());
            }
        }
    }

    // CREATE
    private static void tambahBarang(ArrayList<Barang> list, Scanner scn) throws IOException {
        boolean ulang = true;
        while (ulang) {
            System.out.print("Nama Barang: ");
            String nama = scn.nextLine();
            System.out.print("Jumlah Stok: ");
            int stok = Integer.parseInt(scn.nextLine());
            System.out.print("Tahun beli Barang: ");
            int tahunBeli = Integer.parseInt(scn.nextLine());
            list.add(new Barang(nama, stok, tahunBeli));
            simpanDatabase(list);
            System.out.println("Barang berhasil ditambahkan!");
            System.out.print("Tambah barang lagi? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");

        }

    }

    // READ
    private static ArrayList<Barang> bacaDatabase() throws IOException {
        ArrayList<Barang> list = new ArrayList<>();
        File file = new File("DatabaseBarang.txt");
        if (!file.exists()) {
            return list;
        }

        try (Scanner scn = new Scanner(file)) {
            while (scn.hasNextLine()) {
                String line = scn.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] obj = line.split("\\|");
                if (obj.length < 3) {
                    continue;
                }
                Barang b = new Barang(obj[0], Integer.parseInt(obj[1]), Integer.parseInt(obj[2]));
                list.add(b);
            }
        }
        return list;
    }

    private static void tampilkanBarang(ArrayList<Barang> list) {
        if (list.isEmpty()) {
            System.out.println("Inventory kosong.");
            return;
        }
        String headerFmt = "%-4s %-20s %-10s %-12s%n";
        System.out.printf(headerFmt, "No", "Nama Barang", "Stok", "Tahun beli");
        System.out.println("==========================================================");
        String rowFmt = "%-4d %-20s %-10d %-12d%n";
        for (int i = 0; i < list.size(); i++) {
            Barang b = list.get(i);
            System.out.printf(rowFmt, i + 1, b.nama(), b.stok(), b.tahunBeli());
        }
    }

    // UPDATE
    private static void ubahBarang(ArrayList<Barang> list, Scanner scn) throws IOException {
        tampilkanBarang(list);
        boolean ulang = true;
        while (ulang) {
            System.out.print("Nomor barang yang diubah: ");
            int idx = Integer.parseInt(scn.nextLine()) - 1;
            if (idx >= 0 && idx < list.size()) {
                System.out.print("Nama Barang Baru: ");
                String nama = scn.nextLine();
                System.out.print("Jumlah Stok Baru: ");
                int stok = Integer.parseInt(scn.nextLine());
                System.out.print("Tahun beli Barang Baru: ");
                int tahunBeli = Integer.parseInt(scn.nextLine());
                list.set(idx, new Barang(nama, stok, tahunBeli));
                simpanDatabase(list);
                System.out.println("Barang berhasil diperbarui!");
            } else {
                System.out.println("Nomor barang tidak valid!");
            }
            System.out.print("Ubah barang lain? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    // DELETE
    private static void hapusBarang(ArrayList<Barang> list, Scanner scn) throws IOException {
        tampilkanBarang(list);
        boolean ulang = true;
        while (ulang) {
            System.out.print("Nomor barang yang dihapus: ");
            int idx = Integer.parseInt(scn.nextLine()) - 1;
            if (idx >= 0 && idx < list.size()) {
                list.remove(idx);
                simpanDatabase(list);
                System.out.println("Barang berhasil dihapus!");
            } else {
                System.out.println("Nomor barang tidak valid!");
            }
            System.out.print("Hapus barang lain? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    // Cari semua barang dengan tahun beli paling lama
    private static void cariBarangTahunTerlama(ArrayList<Barang> list) {
        if (list.isEmpty()) {
            System.out.println("Inventory kosong.");
            return;
        }

        // Cari tahun paling kecil
        int tahunTerkecil = list.get(0).tahunBeli();
        for (Barang b : list) {
            if (b.tahunBeli() < tahunTerkecil) {
                tahunTerkecil = b.tahunBeli();
            }
        }

        System.out.println("\n=== Barang dengan Tahun Terlama (" + tahunTerkecil + ") ===");
        for (Barang b : list) {
            if (b.tahunBeli() == tahunTerkecil) {
                System.out.printf("Nama: %s | Stok: %d | Tahun Beli: %d%n",
                        b.nama(), b.stok(), b.tahunBeli());
            }
        }
    }

    // ==========================================================
    // MODUL 2: PENDAPATAN HARIAN
    // ==========================================================
    private static void menuPendapatan(Scanner scn) throws IOException {
        ArrayList<Pendapatan> list = bacaDatabasePendapatan();
        boolean jalan = true;
        while (jalan) {
            System.out.println("\n==========================================");
            System.out.println(">|< APLIKASI PENDAPATAN HARIAN BY VITO >|<");
            System.out.println("==========================================\n");
            System.out.println("1. Tambah Pendapatan");
            System.out.println("2. Lihat Pendapatan");
            System.out.println("3. Ubah Pendapatan");
            System.out.println("4. Hapus Pendapatan");
            System.out.println("5. Cari Pendapatan Terendah");
            System.out.println("6. Kembali");
            System.out.print("\nPilih opsi: ");
            String pilih = scn.nextLine();
            switch (pilih) {
                case "1" ->
                    tambahPendapatan(list, scn);
                case "2" ->
                    tampilkanPendapatan(list);
                case "3" ->
                    ubahPendapatan(list, scn);
                case "4" ->
                    hapusPendapatan(list, scn);
                case "5" ->
                    cariPendapatanTerendah(list);
                case "6" ->
                    jalan = false;
            }
        }
    }

    private static void simpanDatabasePendapatan(ArrayList<Pendapatan> list) throws IOException {
        try (PrintWriter da = new PrintWriter(new FileWriter("DatabasePendapatan.txt", false))) {
            for (Pendapatan p : list) {
                da.println(p.tanggal() + "|" + p.transaksi() + "|" + p.total());
            }
        }
    }

    private static ArrayList<Pendapatan> bacaDatabasePendapatan() throws IOException {
        ArrayList<Pendapatan> list = new ArrayList<>();
        File file = new File("DatabasePendapatan.txt");
        if (!file.exists()) {
            return list;
        }
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] obj = line.split("\\|");
                list.add(new Pendapatan(obj[0], Integer.parseInt(obj[1]), Float.parseFloat(obj[2])));
            }
        }
        return list;
    }

    private static void tampilkanPendapatan(ArrayList<Pendapatan> list) {
        if (list.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        System.out.printf("%-4s %-15s %-15s %-15s%n", "No", "Tanggal", "Transaksi", "Total");
        for (int i = 0; i < list.size(); i++) {
            Pendapatan p = list.get(i);
            System.out.printf("%-4d %-15s %-15d Rp %-12.0f%n", i + 1, p.tanggal(), p.transaksi(), p.total());
        }
    }

    private static void tambahPendapatan(ArrayList<Pendapatan> list, Scanner scn) throws IOException {
        boolean ulang = true;
        while (ulang) {
            System.out.print("Tanggal (dd-mm-yyyy): ");
            String tgl = scn.nextLine();
            System.out.print("Jumlah Transaksi: ");
            int trx = Integer.parseInt(scn.nextLine());
            System.out.print("Total: ");
            float total = Float.parseFloat(scn.nextLine());
            list.add(new Pendapatan(tgl, trx, total));
            simpanDatabasePendapatan(list);
            System.out.println("Data tersimpan!");
            System.out.print("\nTambah data lagi? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    private static void ubahPendapatan(ArrayList<Pendapatan> list, Scanner scn) throws IOException {
        boolean ulang = true;
        while (ulang) {
            tampilkanPendapatan(list);
            System.out.print("Nomor data yang diubah: ");
            int idx = Integer.parseInt(scn.nextLine()) - 1;
            if (idx >= 0 && idx < list.size()) {
                System.out.print("Tanggal Baru: ");
                String tgl = scn.nextLine();
                System.out.print("Transaksi Baru: ");
                int trx = Integer.parseInt(scn.nextLine());
                System.out.print("Total Baru: ");
                float total = Float.parseFloat(scn.nextLine());
                list.set(idx, new Pendapatan(tgl, trx, total));
                simpanDatabasePendapatan(list);
                System.out.println("Data diperbarui!");
            }
            System.out.print("\nUbah data lain? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    private static void hapusPendapatan(ArrayList<Pendapatan> list, Scanner scn) throws IOException {
        tampilkanPendapatan(list);
        System.out.print("Nomor data yang dihapus: ");
        int idx = Integer.parseInt(scn.nextLine()) - 1;
        if (idx >= 0 && idx < list.size()) {
            list.remove(idx);
            simpanDatabasePendapatan(list);
            System.out.println("Data dihapus!");
        }
    }

    private static void cariPendapatanTerendah(ArrayList<Pendapatan> list) throws IOException {
        if (list.isEmpty()) {
            return;
        }
        Pendapatan min = list.get(0);
        for (Pendapatan p : list) {
            if (p.total() < min.total()) {
                min = p;
            }
        }
        System.out.printf("Terendah -> Tanggal: %s | Total: Rp %.0f%n", min.tanggal(), min.total());
        try (PrintWriter out = new PrintWriter(new FileWriter("PendapatanTerendah.txt", false))) {
            out.printf("Tanggal: %s | Total: Rp %.0f%n", min.tanggal(), min.total());
        }
    }

    // ==========================================================
    // MODUL 3: DATA KARYAWAN
    // ==========================================================
    private static void menuKaryawan(Scanner scn) throws IOException {
        ArrayList<Karyawan> list = bacaDatabaseKaryawan();
        boolean jalan = true;
        while (jalan) {
            System.out.println("\n========================================");
            System.out.println(">|< APLIKASI DATA KARYAWAN BY SANTO >|<");
            System.out.println("========================================\n");
            System.out.println("1. Tambah Karyawan");
            System.out.println("2. Lihat Karyawan");
            System.out.println("3. Ubah Karyawan");
            System.out.println("4. Hapus Karyawan");
            System.out.println("5. Cari Upah Tertinggi");
            System.out.println("6. Kembali");
            System.out.print("\nPilih opsi: ");
            String pilih = scn.nextLine();
            switch (pilih) {
                case "1" ->
                    tambahKaryawan(list, scn);
                case "2" ->
                    tampilkanKaryawan(list);
                case "3" ->
                    ubahKaryawan(list, scn);
                case "4" ->
                    hapusKaryawan(list, scn);
                case "5" ->
                    cariUpahTertinggi(list);
                case "6" ->
                    jalan = false;
            }
        }
    }

    private static void simpanDatabaseKaryawan(ArrayList<Karyawan> list) throws IOException {
        try (PrintWriter da = new PrintWriter(new FileWriter("DatabaseKaryawan.txt", false))) {
            for (Karyawan k : list) {
                da.println(k.nama() + "|" + k.jabatan() + "|" + k.status() + "|" + k.upah());
            }
        }
    }

    private static ArrayList<Karyawan> bacaDatabaseKaryawan() throws IOException {
        ArrayList<Karyawan> list = new ArrayList<>();
        File file = new File("DatabaseKaryawan.txt");
        if (!file.exists()) {
            return list;
        }
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] obj = line.split("\\|");
                list.add(new Karyawan(obj[0], obj[1], obj[2], Integer.parseInt(obj[3])));
            }
        }
        return list;
    }

    private static void tampilkanKaryawan(ArrayList<Karyawan> list) {
        if (list.isEmpty()) {
            System.out.println("Data karyawan kosong.");
            return;
        }
        System.out.printf("%-4s %-11s %-11s %-18s %-15s\n", "No", "Nama", "Jabatan", "Status", "Upah");
        System.out.println("================================================================");
        for (int i = 0; i < list.size(); i++) {
            Karyawan k = list.get(i);
            System.out.printf("%-4d %-11s %-11s %-18s Rp %,d\n", i + 1, k.nama(), k.jabatan(), k.status(), k.upah());
        }
    }

    private static void tambahKaryawan(ArrayList<Karyawan> list, Scanner scn) throws IOException {
        boolean ulang = true;
        while (ulang) {
            System.out.print("Nama: ");
            String nama = scn.nextLine();
            System.out.print("Jabatan: ");
            String jab = scn.nextLine();
            System.out.print("Status: ");
            String status = scn.nextLine();
            System.out.print("Upah: ");
            int upah = Integer.parseInt(scn.nextLine());
            list.add(new Karyawan(nama, jab, status, upah));
            simpanDatabaseKaryawan(list);
            System.out.println("Karyawan ditambahkan!");
            System.out.print("Tambah karyawan lagi? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    private static void ubahKaryawan(ArrayList<Karyawan> list, Scanner scn) throws IOException {
        tampilkanKaryawan(list);
        boolean ulang = true;
        while (ulang) {
            System.out.print("Nomor data yang diubah: ");
            int idx = Integer.parseInt(scn.nextLine()) - 1;
            if (idx >= 0 && idx < list.size()) {
                System.out.print("Nama Baru: ");
                String nama = scn.nextLine();
                System.out.print("Jabatan Baru: ");
                String jab = scn.nextLine();
                System.out.print("Status Baru: ");
                String status = scn.nextLine();
                System.out.println("");
                System.out.print("Upah Baru: ");
                int upah = Integer.parseInt(scn.nextLine());
                list.set(idx, new Karyawan(nama, jab, status, upah));
                simpanDatabaseKaryawan(list);
                System.out.println("Data diperbarui!");
            }
            System.out.print("Ubah data lain? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    private static void hapusKaryawan(ArrayList<Karyawan> list, Scanner scn) throws IOException {
        tampilkanKaryawan(list);
        boolean ulang = true;
        while (ulang) {
            System.out.print("Nomor data yang dihapus: ");
            int idx = Integer.parseInt(scn.nextLine()) - 1;
            if (idx >= 0 && idx < list.size()) {
                list.remove(idx);
                simpanDatabaseKaryawan(list);
                System.out.println("Data dihapus!");
            }
            System.out.print("Hapus data lain? (y/n): ");
            ulang = scn.nextLine().equalsIgnoreCase("y");
        }
    }

    private static void cariUpahTertinggi(ArrayList<Karyawan> list) throws IOException {
        if (list.isEmpty()) {
            return;
        }
        Karyawan max = list.get(0);
        for (Karyawan k : list) {
            if (k.upah() > max.upah()) {
                max = k;
            }
        }
        System.out.println("\n=== Karyawan dengan gaji tertinggi===\n");
        System.out.printf("%-8s  %-11s  %-18s  %-15s\n", "Nama", "Posisi", "Status", "Upah");
        System.out.println("===========================================================");

        for (Karyawan k : list) {
            if (k.upah() == max.upah()) {
                System.out.printf("%-8s  %-11s  %-18s  Rp %,d\n", k.nama(), k.jabatan(), k.status(), k.upah());
            }
        }
        System.out.println();
    }

    // ==========================================================
    // MODUL 4: INVENTORY BAHAN MASAK
    // ==========================================================
    private static void menuBahanMasak(Scanner scn) throws IOException {
        ArrayList<BahanMasak> bahanList = new ArrayList<>();
        ArrayList<BahanMasak> bacaBahan = new ArrayList<>();
        bacaBahan = siang();
        bahanList.addAll(bacaBahan);
        malam(bahanList);
        adminMenu(bahanList, scn);
    }

    private static void tampilkan(ArrayList<BahanMasak> dt_list) {
        System.out.println("\n--- DAFTAR BAHAN MASAK ---");
        String headerFmt = "%-4s %-20s %-12s %-20s%n";
        System.out.printf(headerFmt, "No", "bahan", "Stok", "Harga");
        System.out.println("============================================================================");
        String rowFmt = "%-4d %-20s %-12d %-20.2f%n";
        for (int i = 0; i < dt_list.size(); i++) {
            BahanMasak m = dt_list.get(i);
            System.out.printf(rowFmt, i + 1, m.bahan(), m.stok(), m.harga());
        }
    }

    private static void malam(ArrayList<BahanMasak> dt_list) throws IOException {
        try (PrintWriter da = new PrintWriter(new FileWriter("Base bahan masak.txt", false))) {
            for (BahanMasak bahan : dt_list) {
                da.println(bahan.bahan() + "," + bahan.stok() + "," + bahan.harga());
            }
        }
    }

    private static ArrayList<BahanMasak> siang() throws IOException {
        ArrayList<BahanMasak> Rh = new ArrayList<>();
        File dl = new File("Base bahan masak.txt");
        if (!dl.exists()) {
            return Rh;
        }
        try (Scanner scn = new Scanner(dl)) {
            while (scn.hasNextLine()) {
                String menutext = scn.nextLine();
                if (menutext.trim().isEmpty()) {
                    continue;
                }
                String[] obj = menutext.split("\\,");
                if (obj.length < 3) {
                    continue;
                }
                BahanMasak dt = new BahanMasak(obj[0], Integer.parseInt(obj[1]), Float.parseFloat(obj[2]));
                Rh.add(dt);
            }
        }
        return Rh;
    }

    private static void adminMenu(ArrayList<BahanMasak> kamar, Scanner scn) throws IOException {
        boolean back = false;
        while (!back) {
            System.out.println("\n=================================================");
            System.out.println(">|< APLIKASI LOGISTIK BAHAN DAPUR BY RAMADANI >|<");
            System.out.println("================================================\n");
            System.out.println("1. Tambah Bahan (Create)");
            System.out.println("2. Lihat Bahan (Read)");
            System.out.println("3. Ubah Bahan (Update)");
            System.out.println("4. Hapus Bahan (Delete)");
            System.out.println("5. Urutkan Harga Barang (Extra)");
            System.out.println("6. Kembali");
            System.out.print("\nPilih: ");
            String pilih = scn.nextLine();

            switch (pilih) {
                case "1" -> { // CREATE
                    boolean ulang = true;
                    while (ulang) {
                        System.out.print("Nama Bahan: ");
                        String mkn = scn.nextLine();
                        System.out.print("Stok Bahan: ");
                        int stk = Integer.parseInt(scn.nextLine());
                        System.out.print("Harga Bahan: ");
                        float hm = Float.parseFloat(scn.nextLine());
                        kamar.add(new BahanMasak(mkn, stk, hm));
                        malam(kamar);
                        System.out.println("Menu berhasil ditambahkan!");
                        System.out.print("Tambah bahan lagi? (y/n): ");
                        ulang = scn.nextLine().equalsIgnoreCase("y");
                    }
                }
                case "2" -> // READ
                    tampilkan(kamar);
                case "3" -> { // UPDATE
                    tampilkan(kamar);
                    boolean ulang = true;
                    while (ulang) {
                        System.out.print("Nomor bahan yang diubah: ");
                        int idx = Integer.parseInt(scn.nextLine()) - 1;
                        if (idx >= 0 && idx < kamar.size()) {
                            System.out.print("Nama Bahan Baru: ");
                            String mkn = scn.nextLine();
                            System.out.print("Stok Bahan Baru: ");
                            int stk = Integer.parseInt(scn.nextLine());
                            System.out.print("Harga Bahan Baru: ");
                            float hm = Float.parseFloat(scn.nextLine());
                            kamar.set(idx, new BahanMasak(mkn, stk, hm));
                            malam(kamar);
                            System.out.println("Menu berhasil diperbarui!");
                        }
                        System.out.print("Ubah bahan lain? (y/n): ");
                        ulang = scn.nextLine().equalsIgnoreCase("y");
                    }
                }
                case "4" -> { // DELETE
                    tampilkan(kamar);
                    boolean ulang = true;
                    while (ulang) {
                        System.out.print("Nomor menu yang dihapus: ");
                        int idx = Integer.parseInt(scn.nextLine()) - 1;
                        if (idx >= 0 && idx < kamar.size()) {
                            kamar.remove(idx);
                            malam(kamar);
                            System.out.println("Menu berhasil dihapus!");
                        }
                        System.out.print("Hapus bahan lain? (y/n): ");
                        ulang = scn.nextLine().equalsIgnoreCase("y");
                    }
                }
                case "5" -> { // EXTRA
                    kamar.sort((b1, b2) -> Float.compare(b1.harga(), b2.harga()));
                    malam(kamar);
                    System.out.println("Daftar bahan berhasil diurutkan berdasarkan harga!");
                    tampilkan(kamar);
                }
                case "6" -> {
                    malam(kamar);
                    back = true;
                }
            }
        }
    }

    // ==========================================================
    // MODUL 5: INVENTORY MENU MAKANAN DAN MINUMAN
    // ==========================================================
    private static void menuMenuMakanan(Scanner scn) throws IOException {

        ArrayList<menu> bacamenu = menampilkan();

        menuAdmin(bacamenu, scn);

    }

    private static void database(ArrayList<menu> rhp) throws IOException {
        try (PrintWriter da = new PrintWriter(new FileWriter("menu makanan.txt", false))) {
            for (int i = 0; i < rhp.size(); i++) {
                menu nj = rhp.get(i);
                da.println(nj.makanan() + "|" + nj.harga() + "|" + nj.minuman() + "|" + nj.total());
            }
        }
    }

    private static ArrayList<menu> menampilkan() throws IOException {
        ArrayList<menu> Rh = new ArrayList<>();
        File dl = new File("menu makanan.txt");
        if (!dl.exists()) {
            return Rh;
        }
        try (Scanner scn = new Scanner(dl)) {
            while (scn.hasNextLine()) {
                String menutext = scn.nextLine();
                if (menutext.trim().isEmpty()) {
                    continue;
                }
                String[] obj = menutext.split("\\|");
                if (obj.length < 4) {
                    continue;
                }
                menu dt = new menu(obj[0], Integer.parseInt(obj[1]), obj[2], Integer.parseInt(obj[3]));
                Rh.add(dt);
            }
        }
        return Rh;
    }

    private static void tampilan(ArrayList<menu> dt_list, Scanner scn) {
        String headerFmt = "%-4s %-18s %-12s %-18s %-12s%n";
        System.out.printf(headerFmt, "No", "Menu makanan", "Harga", "Menu Minuman", "Harga");
        System.out.println("================================================================================");
        String rowFmt = "%-4d %-18s Rp %,d    %-18s Rp %,d%n";
        for (int i = 0; i < dt_list.size(); i++) {
            menu m = dt_list.get(i);
            System.out.printf(rowFmt, i + 1, m.makanan(), m.harga(), m.minuman(), m.total());
        }
    }

    private static void prosesPemesanan(ArrayList<menu> dt_list, Scanner scn) {
        int grandTotal = 0;
        String lagi = "y";

        while (lagi.equalsIgnoreCase("y")) {
            System.out.print("\nPilih Nomor Menu (1-" + dt_list.size() + "): ");
            int no = scn.nextInt();
            scn.nextLine();

            if (no > 0 && no <= dt_list.size()) {
                System.out.print("Masukkan Jumlah Porsi: ");
                int jumlah = scn.nextInt();
                scn.nextLine();

                menu pilihan = dt_list.get(no - 1);
                int hargaSatuan = pilihan.harga() + pilihan.total();
                int subTotal = hargaSatuan * jumlah;
                grandTotal += subTotal;

                System.out.printf("Item: %s & %s | Subtotal: Rp %,d %n",
                        pilihan.makanan(), pilihan.minuman(), subTotal);
            }

            if (no <= 0 || no > dt_list.size()) {
                System.out.println("Nomor menu tidak tersedia.");
            }

            System.out.print("Tambah pesanan lagi? (y/n): ");
            lagi = scn.next();
            scn.nextLine();
        }

        System.out.println("\n========================================");
        System.out.printf("TOTAL AKHIR: Rp %,d %n", grandTotal);
        System.out.println("========================================");

        System.out.println("\nTekan Enter untuk kembali ke Menu Utama...");
        scn.nextLine();

    }

    private static void menuAdmin(ArrayList<menu> dt_list, Scanner scn) throws IOException {
        boolean back = false;
        while (!back) {
            System.out.println("\n================================================");
            System.out.println(">|< APLIKASI MENU MAKANAN & MINUMAN BY RIFKI >|<");
            System.out.println("================================================\n");
            System.out.println("1. Tambah Menu (Create)");
            System.out.println("2. Lihat Menu (Read)");
            System.out.println("3. Ubah Menu (Update)");
            System.out.println("4. Hapus Menu (Delete)");
            System.out.println("5. Pesan menu (Extra)");
            System.out.println("6. Kembali");
            System.out.print("\nPilih: ");
            String pilih = scn.nextLine();

            switch (pilih) {
                case "1" -> { // CREATE
                    boolean ulang = true;
                    while (ulang) {
                        System.out.print("Nama Makanan: ");
                        String mkn = scn.nextLine();
                        System.out.print("Harga Makanan: ");
                        int hm = Integer.parseInt(scn.nextLine());
                        System.out.print("Nama Minuman: ");
                        String mnm = scn.nextLine();
                        System.out.print("Harga Minuman: ");
                        int hmn = Integer.parseInt(scn.nextLine());
                        dt_list.add(new menu(mkn, hm, mnm, hmn));
                        database(dt_list);
                        System.out.println("Menu berhasil ditambahkan!");
                        System.out.print("\nTambah menu lagi? (y/n): ");
                        ulang = scn.nextLine().equalsIgnoreCase("y");
                    }
                }
                case "2" ->
                    tampilan(dt_list, scn); // READ
                case "3" -> { // UPDATE
                    boolean ulang = true;
                    while (ulang) {
                        tampilan(dt_list, scn);
                        System.out.print("Nomor menu yang diubah: ");
                        int idx = Integer.parseInt(scn.nextLine()) - 1;
                        if (idx >= 0 && idx < dt_list.size()) {
                            System.out.print("Nama Makanan Baru: ");
                            String mkn = scn.nextLine();
                            System.out.print("Harga Makanan Baru: ");
                            int hm = Integer.parseInt(scn.nextLine());
                            System.out.print("Nama Minuman Baru: ");
                            String mnm = scn.nextLine();
                            System.out.print("Harga Minuman Baru: ");
                            int hmn = Integer.parseInt(scn.nextLine());
                            dt_list.set(idx, new menu(mkn, hm, mnm, hmn));

                            database(dt_list);
                            System.out.println("Menu berhasil diperbarui!");
                        }
                        System.out.print("Ubah menu lain? (y/n): ");
                        ulang = scn.nextLine().equalsIgnoreCase("y");
                    }
                    tampilan(dt_list, scn);
                }
                case "4" -> { // DELETE
                    boolean ulang = true;
                    while (ulang) {
                        tampilan(dt_list, scn);
                        System.out.print("Nomor menu yang dihapus: ");
                        int idx = Integer.parseInt(scn.nextLine()) - 1;
                        if (idx >= 0 && idx < dt_list.size()) {
                            dt_list.remove(idx);
                            database(dt_list);
                            System.out.println("Menu berhasil dihapus!");
                        }
                        System.out.print("Hapus menu lain? (y/n): ");
                        ulang = scn.nextLine().equalsIgnoreCase("y");
                    }
                    tampilan(dt_list, scn);
                }
                case "5" -> {
                    tampilan(dt_list, scn);
                    prosesPemesanan(dt_list, scn);
                }
                default ->
                    System.out.println("Pilihan tidak valid");

                case "6" -> {
                    database(dt_list);
                    back = true;
                }

            }
        }

    }
}
