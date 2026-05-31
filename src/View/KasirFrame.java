/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.ProdukController;
import Controller.TransaksiController;
import Model.ModelSupermarket.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author LENOVO
 */

public class KasirFrame extends JFrame {

    private User user;
    private ProdukController produkCtrl;
    private TransaksiController transaksiCtrl;

    private static final NumberFormat FMT = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    // Panel kiri - daftar produk
    private JTable tabelProduk;
    private DefaultTableModel modelProduk;
    private JTextField txtCari;

    // Panel kanan - keranjang
    private JTable tabelKeranjang;
    private DefaultTableModel modelKeranjang;
    private JLabel lblTotal;
    private List<DetailTransaksi> keranjang = new ArrayList<>();

    public KasirFrame(User user) {
        this.user       = user;
        produkCtrl      = new ProdukController();
        transaksiCtrl   = new TransaksiController();

        setTitle("Kasir - " + user.getNamaLengkap());
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(0x117a65));
        JLabel lblTitle = new JLabel("  🧾 Kasir: " + user.getNamaLengkap(), SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 0));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        JButton btnLogout = buatTombol("Logout", new Color(0xe74c3c));
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame(); });
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlRight.setOpaque(false);
        pnlRight.add(btnLogout);
        pnlHeader.add(pnlRight, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // Konten utama
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            buildPanelProduk(), buildPanelKeranjang());
        split.setDividerLocation(560);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        muatProduk();
        setVisible(true);
    }

    // ===================== PANEL DAFTAR PRODUK =====================
    private JPanel buildPanelProduk() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Produk"));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.add(new JLabel("Cari Produk:"));
        txtCari = new JTextField(18);
        pnlSearch.add(txtCari);
        JButton btnCari    = buatTombol("Cari",    new Color(0x2980b9));
        JButton btnRefresh = buatTombol("Refresh", new Color(0x117a65));
        pnlSearch.add(btnCari); pnlSearch.add(btnRefresh);
        panel.add(pnlSearch, BorderLayout.NORTH);

        String[] kolom = {"ID", "Nama Produk", "Kategori", "Harga", "Stok"};
        modelProduk = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelProduk = new JTable(modelProduk);
        styleTable(tabelProduk);
        tabelProduk.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabelProduk.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelProduk.getColumnModel().getColumn(4).setPreferredWidth(50);
        panel.add(new JScrollPane(tabelProduk), BorderLayout.CENTER);

        // Tambah ke keranjang
        JPanel pnlBawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBawah.add(new JLabel("Jumlah:"));
        JSpinner spJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spJumlah.setPreferredSize(new Dimension(70, 28));
        pnlBawah.add(spJumlah);
        JButton btnTambahKeranjang = buatTombol("+ Tambah ke Keranjang", new Color(0x117a65));
        btnTambahKeranjang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlBawah.add(btnTambahKeranjang);
        panel.add(pnlBawah, BorderLayout.SOUTH);

        btnCari.addActionListener(e -> {
            modelProduk.setRowCount(0);
            for (Produk p : produkCtrl.cariProduk(txtCari.getText()))
                tambahBarisProduk(p);
        });
        btnRefresh.addActionListener(e -> muatProduk());
        btnTambahKeranjang.addActionListener(e -> {
            int baris = tabelProduk.getSelectedRow();
            if (baris < 0) {
                JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int idProduk = (int) modelProduk.getValueAt(baris, 0);
            String nama  = modelProduk.getValueAt(baris, 1).toString();
            int stok     = (int) modelProduk.getValueAt(baris, 4);
            double harga = produkCtrl.getProdukById(idProduk).getHarga();
            int jumlah   = (int) spJumlah.getValue();

            if (jumlah > stok) {
                JOptionPane.showMessageDialog(this, "Jumlah melebihi stok tersedia (" + stok + ")!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            tambahKeKeranjang(idProduk, nama, jumlah, harga);
        });

        return panel;
    }

    // ===================== PANEL KERANJANG =====================
    private JPanel buildPanelKeranjang() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));

        String[] kolom = {"Nama Produk", "Harga", "Jumlah", "Subtotal"};
        modelKeranjang = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelKeranjang = new JTable(modelKeranjang);
        styleTable(tabelKeranjang);
        panel.add(new JScrollPane(tabelKeranjang), BorderLayout.CENTER);

        // Panel bawah keranjang
        JPanel pnlBawah = new JPanel(new BorderLayout());
        pnlBawah.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

        lblTotal = new JLabel("TOTAL: Rp 0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(0x117a65));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 5));
        pnlBawah.add(lblTotal, BorderLayout.NORTH);

        JPanel pnlBtn = new JPanel(new GridLayout(1, 3, 8, 0));
        JButton btnHapusItem  = buatTombol("Hapus Item",    new Color(0xe74c3c));
        JButton btnKosongkan  = buatTombol("Kosongkan",     new Color(0x7f8c8d));
        JButton btnBayar      = buatTombol("💳 BAYAR",       new Color(0x117a65));
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlBtn.add(btnHapusItem); pnlBtn.add(btnKosongkan); pnlBtn.add(btnBayar);
        pnlBawah.add(pnlBtn, BorderLayout.SOUTH);
        panel.add(pnlBawah, BorderLayout.SOUTH);

        btnHapusItem.addActionListener(e -> {
            int baris = tabelKeranjang.getSelectedRow();
            if (baris < 0) return;
            keranjang.remove(baris);
            modelKeranjang.removeRow(baris);
            updateTotal();
        });
        btnKosongkan.addActionListener(e -> {
            keranjang.clear();
            modelKeranjang.setRowCount(0);
            updateTotal();
        });
        btnBayar.addActionListener(e -> aksiProsesBayar());

        return panel;
    }

    // ===================== LOGIKA =====================
    private void muatProduk() {
        modelProduk.setRowCount(0);
        for (Produk p : produkCtrl.semuaProduk())
            tambahBarisProduk(p);
    }

    private void tambahBarisProduk(Produk p) {
        modelProduk.addRow(new Object[]{p.getId(), p.getNama(), p.getNamaKategori(), FMT.format(p.getHarga()), p.getStok()});
    }

    private void tambahKeKeranjang(int idProduk, String nama, int jumlah, double harga) {
        // Cek apakah produk sudah ada di keranjang → update jumlah
        for (int i = 0; i < keranjang.size(); i++) {
            if (keranjang.get(i).getIdProduk() == idProduk) {
                DetailTransaksi d = keranjang.get(i);
                d.setJumlah(d.getJumlah() + jumlah);
                modelKeranjang.setValueAt(d.getJumlah(),             i, 2);
                modelKeranjang.setValueAt(FMT.format(d.getSubtotal()), i, 3);
                updateTotal();
                return;
            }
        }
        // Produk baru
        DetailTransaksi d = new DetailTransaksi(idProduk, nama, jumlah, harga);
        keranjang.add(d);
        modelKeranjang.addRow(new Object[]{nama, FMT.format(harga), jumlah, FMT.format(d.getSubtotal())});
        updateTotal();
    }

    private void updateTotal() {
        double total = transaksiCtrl.hitungTotal(keranjang);
        lblTotal.setText("TOTAL: " + FMT.format(total));
    }

    private void aksiProsesBayar() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = transaksiCtrl.hitungTotal(keranjang);

        // Dialog input uang tunai
        String inputStr = JOptionPane.showInputDialog(this,
            "Total: " + FMT.format(total) + "\nMasukkan uang bayar (Rp):",
            "Proses Pembayaran", JOptionPane.PLAIN_MESSAGE);
        if (inputStr == null) return;

        try {
            double bayar = Double.parseDouble(inputStr.trim().replace(",", "").replace(".", ""));
            if (bayar < total) {
                JOptionPane.showMessageDialog(this, "Nominal uang kurang!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idTrx = transaksiCtrl.prosesTransaksi(keranjang);
            double kembalian = bayar - total;

            // Struk
            StringBuilder struk = new StringBuilder();
            struk.append("===== STRUK BELANJA =====\n");
            struk.append("ID Transaksi : #").append(idTrx).append("\n");
            struk.append("Kasir        : ").append(user.getNamaLengkap()).append("\n");
            struk.append("-------------------------\n");
            for (DetailTransaksi d : keranjang) {
                struk.append(String.format("%-18s x%d\n  %s\n", d.getNamaProduk(), d.getJumlah(), FMT.format(d.getSubtotal())));
            }
            struk.append("-------------------------\n");
            struk.append("Total      : ").append(FMT.format(total)).append("\n");
            struk.append("Bayar      : ").append(FMT.format(bayar)).append("\n");
            struk.append("Kembalian  : ").append(FMT.format(kembalian)).append("\n");
            struk.append("=========================\n");
            struk.append("     Terima kasih!       ");

            JTextArea txStruk = new JTextArea(struk.toString());
            txStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
            txStruk.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(txStruk), "Struk Belanja", JOptionPane.INFORMATION_MESSAGE);

            // Reset keranjang & refresh stok
            keranjang.clear();
            modelKeranjang.setRowCount(0);
            updateTotal();
            muatProduk();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input uang tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(26);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(0x117a65));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(0xd5f5e3));
        t.setGridColor(new Color(0xddd));
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
