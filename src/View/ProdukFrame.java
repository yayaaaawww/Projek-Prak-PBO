/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.ModelSupermarket.Produk;
import Model.ModelSupermarket.ProdukDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author LENOVO
 */

public class ProdukFrame extends JFrame {

    private ProdukDAO dao = new ProdukDAO();

    // Komponen form
    private JTextField txtId, txtNama, txtHarga, txtStok, txtKategoriId, txtCari;
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JButton btnTambah, btnUbah, btnHapus, btnBersihkan, btnCari, btnRefresh;

    private static final NumberFormat FMT = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public ProdukFrame() {
        setTitle("Manajemen Supermarket - Data Produk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel judul
        JLabel lblJudul = new JLabel("MANAJEMEN SUPERMARKET", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(0x1a5276));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(lblJudul, BorderLayout.NORTH);

        // Panel kiri - form input
        add(buildFormPanel(), BorderLayout.WEST);

        // Panel tengah - tabel + search
        add(buildTablePanel(), BorderLayout.CENTER);

        muatData();
        setVisible(true);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x2980b9), 1), "Form Produk",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(0x2980b9)
        ));
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBackground(new Color(0xf4f6f9));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // ID (readonly)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Produk:"), gbc);
        gbc.gridy = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(0xe8ecf1));
        panel.add(txtId, gbc);

        // Nama
        gbc.gridy = 2;
        panel.add(new JLabel("Nama Produk:"), gbc);
        gbc.gridy = 3;
        txtNama = new JTextField();
        panel.add(txtNama, gbc);

        // Kategori ID
        gbc.gridy = 4;
        panel.add(new JLabel("ID Kategori (1-5):"), gbc);
        gbc.gridy = 5;
        txtKategoriId = new JTextField();
        panel.add(txtKategoriId, gbc);

        // Harga
        gbc.gridy = 6;
        panel.add(new JLabel("Harga (Rp):"), gbc);
        gbc.gridy = 7;
        txtHarga = new JTextField();
        panel.add(txtHarga, gbc);

        // Stok
        gbc.gridy = 8;
        panel.add(new JLabel("Stok:"), gbc);
        gbc.gridy = 9;
        txtStok = new JTextField();
        panel.add(txtStok, gbc);

        // Tombol-tombol
        gbc.gridy = 10;
        JPanel pnlBtn = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlBtn.setOpaque(false);

        btnTambah    = buatTombol("Tambah",    new Color(0x27ae60));
        btnUbah      = buatTombol("Ubah",      new Color(0xf39c12));
        btnHapus     = buatTombol("Hapus",     new Color(0xe74c3c));
        btnBersihkan = buatTombol("Bersihkan", new Color(0x7f8c8d));

        pnlBtn.add(btnTambah);
        pnlBtn.add(btnUbah);
        pnlBtn.add(btnHapus);
        pnlBtn.add(btnBersihkan);
        panel.add(pnlBtn, gbc);

        // Keterangan kategori
        gbc.gridy = 11;
        JLabel lblKet = new JLabel("<html><small>1=Minuman, 2=Makanan,<br>3=Snack, 4=Kebersihan, 5=Lainnya</small></html>");
        lblKet.setForeground(Color.GRAY);
        panel.add(lblKet, gbc);

        // Events
        btnTambah.addActionListener(e -> aksiTambah());
        btnUbah.addActionListener(e -> aksiUbah());
        btnHapus.addActionListener(e -> aksiHapus());
        btnBersihkan.addActionListener(e -> bersihkanForm());

        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));

        // Search bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setOpaque(false);
        pnlSearch.add(new JLabel("Cari:"));
        txtCari = new JTextField(20);
        pnlSearch.add(txtCari);
        btnCari    = buatTombol("Cari",    new Color(0x2980b9));
        btnRefresh = buatTombol("Refresh", new Color(0x16a085));
        pnlSearch.add(btnCari);
        pnlSearch.add(btnRefresh);
        panel.add(pnlSearch, BorderLayout.NORTH);

        // Tabel
        String[] kolom = {"ID", "Nama Produk", "Kategori", "Harga", "Stok"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setRowHeight(26);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(new Color(0x2980b9));
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(0xd6eaf8));
        tabel.setGridColor(new Color(0xddd));

        tabel.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(60);

        // Klik baris → isi form
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabel.getSelectedRow() >= 0) {
                isiFormDariTabel(tabel.getSelectedRow());
            }
        });

        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);

        btnCari.addActionListener(e -> aksiCari());
        btnRefresh.addActionListener(e -> muatData());

        return panel;
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

    private void muatData() {
        modelTabel.setRowCount(0);
        List<Produk> list = dao.getAll();
        for (Produk p : list) {
            modelTabel.addRow(new Object[]{
                p.getId(), p.getNama(), p.getNamaKategori(),
                FMT.format(p.getHarga()), p.getStok()
            });
        }
        bersihkanForm();
    }

    private void aksiTambah() {
        try {
            Produk p = bacaForm();
            dao.tambah(p);
            JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiUbah() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk dari tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Produk p = bacaForm();
            p.setId(Integer.parseInt(txtId.getText()));
            dao.ubah(p);
            JOptionPane.showMessageDialog(this, "Produk berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiHapus() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk dari tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus produk ini?", "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            dao.hapus(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatData();
        }
    }

    private void aksiCari() {
        String keyword = txtCari.getText().trim();
        if (keyword.isEmpty()) { muatData(); return; }
        modelTabel.setRowCount(0);
        List<Produk> list = dao.cariByNama(keyword);
        for (Produk p : list) {
            modelTabel.addRow(new Object[]{
                p.getId(), p.getNama(), p.getNamaKategori(),
                FMT.format(p.getHarga()), p.getStok()
            });
        }
    }

    private Produk bacaForm() {
        String nama = txtNama.getText().trim();
        String hargaStr = txtHarga.getText().trim().replace(",", "").replace(".", "");
        String stokStr = txtStok.getText().trim();
        String katStr = txtKategoriId.getText().trim();

        if (nama.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty() || katStr.isEmpty()) {
            throw new IllegalArgumentException("Semua field wajib diisi!");
        }
        Produk p = new Produk();
        p.setNama(nama);
        p.setIdKategori(Integer.parseInt(katStr));
        p.setHarga(Double.parseDouble(hargaStr));
        p.setStok(Integer.parseInt(stokStr));
        return p;
    }

    private void isiFormDariTabel(int baris) {
        txtId.setText(modelTabel.getValueAt(baris, 0).toString());
        txtNama.setText(modelTabel.getValueAt(baris, 1).toString());
        // Ambil id kategori dari DAO berdasarkan id produk
        int idProduk = Integer.parseInt(txtId.getText());
        Produk p = dao.cariById(idProduk);
        if (p != null) {
            txtKategoriId.setText(String.valueOf(p.getIdKategori()));
            txtHarga.setText(String.valueOf((int) p.getHarga()));
            txtStok.setText(String.valueOf(p.getStok()));
        }
    }

    private void bersihkanForm() {
        txtId.setText("");
        txtNama.setText("");
        txtKategoriId.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtCari.setText("");
        tabel.clearSelection();
    }
}
