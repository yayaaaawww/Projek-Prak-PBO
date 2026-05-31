/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.ProdukController;
import Controller.KategoriController;
import Controller.TransaksiController;
import Model.ModelSupermarket.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author LENOVO
 */
public class ManajerFrame extends JFrame {

    private User user;
    private ProdukController produkCtrl;
    private KategoriController kategoriCtrl;
    private TransaksiController transaksiCtrl;

    private static final NumberFormat FMT = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Komponen tab Produk
    private JTextField txtId, txtNama, txtHarga, txtStok, txtCari;
    private JComboBox<Kategori> cmbKategori;
    private JTable tabelProduk;
    private DefaultTableModel modelProduk;

    // Komponen tab Transaksi
    private JTable tabelTransaksi;
    private DefaultTableModel modelTransaksi;
    private JTable tabelDetail;
    private DefaultTableModel modelDetail;

    public ManajerFrame(User user) {
        this.user         = user;
        produkCtrl        = new ProdukController();
        kategoriCtrl      = new KategoriController();
        transaksiCtrl     = new TransaksiController();

        setTitle("Dashboard Manajer - " + user.getNamaLengkap());
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(0x1a5276));
        JLabel lblTitle = new JLabel("  🛒 Manajemen Supermarket  |  Manajer: " + user.getNamaLengkap(), SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 0));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(0xe74c3c));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame(); });
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlRight.setOpaque(false);
        pnlRight.add(btnLogout);
        pnlHeader.add(pnlRight, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // Tab utama
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabs.addTab("📦 Data Produk",    buildTabProduk());
        tabs.addTab("🏷️ Kategori",        buildTabKategori());
        tabs.addTab("📋 Riwayat Transaksi", buildTabTransaksi());
        add(tabs, BorderLayout.CENTER);

        muatProduk();
        muatTransaksi();
        setVisible(true);
    }

    // ===================== TAB PRODUK =====================
    private JPanel buildTabProduk() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form kiri
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Form Produk"));
        pnlForm.setPreferredSize(new Dimension(270, 0));
        pnlForm.setBackground(new Color(0xf4f6f9));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"ID Produk:", "Nama Produk:", "Kategori:", "Harga (Rp):", "Stok:"};
        gbc.gridx = 0;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;
            pnlForm.add(new JLabel(labels[i]), gbc);
        }

        gbc.gridy = 1;
        txtId = new JTextField(); txtId.setEditable(false); txtId.setBackground(new Color(0xe8ecf1));
        pnlForm.add(txtId, gbc);
        gbc.gridy = 3;
        txtNama = new JTextField();
        pnlForm.add(txtNama, gbc);
        gbc.gridy = 5;
        cmbKategori = new JComboBox<>();
        muatKomboKategori();
        pnlForm.add(cmbKategori, gbc);
        gbc.gridy = 7;
        txtHarga = new JTextField();
        pnlForm.add(txtHarga, gbc);
        gbc.gridy = 9;
        txtStok = new JTextField();
        pnlForm.add(txtStok, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(12, 8, 5, 8);
        JPanel pnlBtn = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlBtn.setOpaque(false);
        JButton btnTambah    = buatTombol("Tambah",    new Color(0x27ae60));
        JButton btnUbah      = buatTombol("Ubah",      new Color(0xf39c12));
        JButton btnHapus     = buatTombol("Hapus",     new Color(0xe74c3c));
        JButton btnBersihkan = buatTombol("Bersihkan", new Color(0x7f8c8d));
        pnlBtn.add(btnTambah); pnlBtn.add(btnUbah);
        pnlBtn.add(btnHapus);  pnlBtn.add(btnBersihkan);
        pnlForm.add(pnlBtn, gbc);
        panel.add(pnlForm, BorderLayout.WEST);

        // Tabel kanan
        JPanel pnlTabel = new JPanel(new BorderLayout(5, 5));
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.add(new JLabel("Cari:"));
        txtCari = new JTextField(20);
        pnlSearch.add(txtCari);
        JButton btnCari    = buatTombol("Cari",    new Color(0x2980b9));
        JButton btnRefresh = buatTombol("Refresh", new Color(0x16a085));
        pnlSearch.add(btnCari); pnlSearch.add(btnRefresh);
        pnlTabel.add(pnlSearch, BorderLayout.NORTH);

        String[] kolom = {"ID", "Nama Produk", "Kategori", "Harga", "Stok"};
        modelProduk = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelProduk = new JTable(modelProduk);
        styleTable(tabelProduk);
        tabelProduk.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelProduk.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelProduk.getColumnModel().getColumn(4).setPreferredWidth(60);
        tabelProduk.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelProduk.getSelectedRow() >= 0)
                isiFormProduk(tabelProduk.getSelectedRow());
        });
        pnlTabel.add(new JScrollPane(tabelProduk), BorderLayout.CENTER);
        panel.add(pnlTabel, BorderLayout.CENTER);

        // Events
        btnTambah.addActionListener(e -> {
            try {
                Kategori k = (Kategori) cmbKategori.getSelectedItem();
                produkCtrl.tambahProduk(txtNama.getText(), k.getIdKategori(),
                    Double.parseDouble(txtHarga.getText()), Integer.parseInt(txtStok.getText()));
                JOptionPane.showMessageDialog(this, "Produk ditambahkan!");
                muatProduk();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnUbah.addActionListener(e -> {
            try {
                Kategori k = (Kategori) cmbKategori.getSelectedItem();
                produkCtrl.ubahProduk(Integer.parseInt(txtId.getText()), txtNama.getText(),
                    k.getIdKategori(), Double.parseDouble(txtHarga.getText()), Integer.parseInt(txtStok.getText()));
                JOptionPane.showMessageDialog(this, "Produk diubah!");
                muatProduk();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            int ok = JOptionPane.showConfirmDialog(this, "Yakin hapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                produkCtrl.hapusProduk(Integer.parseInt(txtId.getText()));
                muatProduk();
            }
        });
        btnBersihkan.addActionListener(e -> bersihkanFormProduk());
        btnCari.addActionListener(e -> {
            modelProduk.setRowCount(0);
            for (Produk p : produkCtrl.cariProduk(txtCari.getText()))
                modelProduk.addRow(new Object[]{p.getId(), p.getNama(), p.getNamaKategori(), FMT.format(p.getHarga()), p.getStok()});
        });
        btnRefresh.addActionListener(e -> muatProduk());

        return panel;
    }

    // ===================== TAB KATEGORI =====================
    private JPanel buildTabKategori() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton btnKelola = buatTombol("Buka Kelola Kategori", new Color(0x1a5276));
        btnKelola.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnKelola.setPreferredSize(new Dimension(250, 45));
        btnKelola.addActionListener(e -> {
            new KategoriFrame(this);
            muatKomboKategori(); // refresh combo setelah ubah kategori
        });
        JPanel pnlCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 100));
        pnlCenter.add(btnKelola);
        panel.add(pnlCenter, BorderLayout.CENTER);
        return panel;
    }

    // ===================== TAB TRANSAKSI =====================
    private JPanel buildTabTransaksi() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabel transaksi atas
        JLabel lblAtas = new JLabel("Semua Riwayat Transaksi");
        lblAtas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblAtas, BorderLayout.NORTH);

        String[] kolomT = {"ID Transaksi", "Tanggal", "Total"};
        modelTransaksi = new DefaultTableModel(kolomT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelTransaksi = new JTable(modelTransaksi);
        styleTable(tabelTransaksi);
        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelTransaksi.getSelectedRow() >= 0) {
                int idTrx = (int) modelTransaksi.getValueAt(tabelTransaksi.getSelectedRow(), 0);
                muatDetailTransaksi(idTrx);
            }
        });

        // Tabel detail bawah
        String[] kolomD = {"Nama Produk", "Harga Satuan", "Jumlah", "Subtotal"};
        modelDetail = new DefaultTableModel(kolomD, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelDetail = new JTable(modelDetail);
        styleTable(tabelDetail);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(tabelTransaksi), new JScrollPane(tabelDetail));
        split.setDividerLocation(300);
        split.setResizeWeight(0.6);
        panel.add(split, BorderLayout.CENTER);

        JButton btnRefresh = buatTombol("Refresh", new Color(0x16a085));
        btnRefresh.addActionListener(e -> muatTransaksi());
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBot.add(btnRefresh);
        panel.add(pnlBot, BorderLayout.SOUTH);

        return panel;
    }

    // ===================== HELPER =====================
    private void muatProduk() {
        modelProduk.setRowCount(0);
        for (Produk p : produkCtrl.semuaProduk())
            modelProduk.addRow(new Object[]{p.getId(), p.getNama(), p.getNamaKategori(), FMT.format(p.getHarga()), p.getStok()});
        bersihkanFormProduk();
    }

    private void muatKomboKategori() {
        cmbKategori.removeAllItems();
        for (Kategori k : kategoriCtrl.semuaKategori())
            cmbKategori.addItem(k);
    }

    private void muatTransaksi() {
        modelTransaksi.setRowCount(0);
        for (Transaksi t : transaksiCtrl.semuaTransaksi())
            modelTransaksi.addRow(new Object[]{
                t.getIdTransaksi(), SDF.format(t.getTanggal()), FMT.format(t.getTotal())
            });
        modelDetail.setRowCount(0);
    }

    private void muatDetailTransaksi(int idTrx) {
        modelDetail.setRowCount(0);
        for (DetailTransaksi d : transaksiCtrl.detailTransaksi(idTrx))
            modelDetail.addRow(new Object[]{
                d.getNamaProduk(), FMT.format(d.getHargaSatuan()), d.getJumlah(), FMT.format(d.getSubtotal())
            });
    }

    private void isiFormProduk(int baris) {
        txtId.setText(modelProduk.getValueAt(baris, 0).toString());
        txtNama.setText(modelProduk.getValueAt(baris, 1).toString());
        Produk p = produkCtrl.getProdukById(Integer.parseInt(txtId.getText()));
        if (p != null) {
            // Hapus (int) agar desimal tidak hilang
            txtHarga.setText(String.valueOf(p.getHarga()));
            txtStok.setText(String.valueOf(p.getStok()));
            for (int i = 0; i < cmbKategori.getItemCount(); i++) {
                if (cmbKategori.getItemAt(i).getIdKategori() == p.getIdKategori()) {
                    cmbKategori.setSelectedIndex(i); break;
                }
            }
        }
    }

    private void bersihkanFormProduk() {
        txtId.setText(""); txtNama.setText("");
        txtHarga.setText(""); txtStok.setText("");
        if (cmbKategori.getItemCount() > 0) cmbKategori.setSelectedIndex(0);
        tabelProduk.clearSelection();
    }

    private void styleTable(JTable t) {
        t.setRowHeight(26);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(0x1a5276));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(0xd6eaf8));
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
