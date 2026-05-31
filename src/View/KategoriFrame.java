/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.KategoriController;
import Model.ModelSupermarket.Kategori;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author LENOVO
 */
public class KategoriFrame extends JDialog {

    private KategoriController controller;
    private JTextField txtId, txtNama;
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JButton btnTambah, btnUbah, btnHapus, btnBersihkan;

    public KategoriFrame(JFrame parent) {
        super(parent, "Kelola Kategori", true);
        controller = new KategoriController();

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(8, 8));

        // Form atas
        // --- PERBAIKAN BUG LAYOUT ---
        JPanel pnlForm = new JPanel(new BorderLayout(5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Form Kategori"));
        
        // Panel khusus untuk input (baris 1)
        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlInput.add(new JLabel("ID:"));
        txtId = new JTextField(4);
        txtId.setEditable(false);
        txtId.setBackground(new Color(0xe8ecf1));
        pnlInput.add(txtId);
        
        pnlInput.add(new JLabel("Nama Kategori:"));
        txtNama = new JTextField(15);
        pnlInput.add(txtNama);

        // Panel khusus untuk tombol (baris 2)
        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnTambah    = buatTombol("Tambah",    new Color(0x27ae60));
        btnUbah      = buatTombol("Ubah",      new Color(0xf39c12));
        btnHapus     = buatTombol("Hapus",     new Color(0xe74c3c));
        btnBersihkan = buatTombol("Bersihkan", new Color(0x7f8c8d));
        
        pnlTombol.add(btnTambah);
        pnlTombol.add(btnUbah);
        pnlTombol.add(btnHapus);
        pnlTombol.add(btnBersihkan);

        // Gabungkan ke pnlForm
        pnlForm.add(pnlInput, BorderLayout.NORTH);
        pnlForm.add(pnlTombol, BorderLayout.CENTER);
        
        // Masukkan ke frame utama
        add(pnlForm, BorderLayout.NORTH);

        // Tabel
        String[] kolom = {"ID", "Nama Kategori"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setRowHeight(24);
        tabel.getTableHeader().setBackground(new Color(0x2980b9));
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabel.getSelectedRow() >= 0) {
                txtId.setText(modelTabel.getValueAt(tabel.getSelectedRow(), 0).toString());
                txtNama.setText(modelTabel.getValueAt(tabel.getSelectedRow(), 1).toString());
            }
        });
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        // Events
        btnTambah.addActionListener(e -> {
            try {
                controller.tambahKategori(txtNama.getText());
                JOptionPane.showMessageDialog(this, "Kategori ditambahkan!");
                muatData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnUbah.addActionListener(e -> {
            if(txtId.getText().isEmpty()){
                JOptionPane.showMessageDialog(this,"Pilih kategori dari tabel terlebih dahulu", "PERINGATAN!", JOptionPane.WARNING_MESSAGE);
            }
            try {
                controller.ubahKategori(Integer.parseInt(txtId.getText()), txtNama.getText());
                JOptionPane.showMessageDialog(this, "Kategori diubah!");
                muatData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            int ok = JOptionPane.showConfirmDialog(this, "Yakin hapus kategori ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    controller.hapusKategori(Integer.parseInt(txtId.getText()));
                    muatData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnBersihkan.addActionListener(e -> { txtId.setText(""); txtNama.setText(""); tabel.clearSelection(); });

        muatData();
        setVisible(true);
    }

    private void muatData() {
        modelTabel.setRowCount(0);
        List<Kategori> list = controller.semuaKategori();
        for (Kategori k : list) {
            modelTabel.addRow(new Object[]{k.getIdKategori(), k.getNamaKategori()});
        }
        txtId.setText(""); txtNama.setText(""); tabel.clearSelection();
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
