/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author LENOVO
 */
import Model.ModelSupermarket.Kategori;
import Model.ModelSupermarket.KategoriDAO;
import java.util.List;

public class KategoriController {

    private KategoriDAO dao;

    public KategoriController() {
        this.dao = new KategoriDAO();
    }

    public void tambahKategori(String nama) {
        if (nama == null || nama.trim().isEmpty())
            throw new IllegalArgumentException("Nama kategori tidak boleh kosong!");
        Kategori k = new Kategori();
        k.setNamaKategori(nama.trim());
        dao.tambah(k);
    }

    public void ubahKategori(int id, String nama) {
        if (id <= 0)
            throw new IllegalArgumentException("Pilih kategori terlebih dahulu!");
        if (nama == null || nama.trim().isEmpty())
            throw new IllegalArgumentException("Nama kategori tidak boleh kosong!");
        Kategori k = new Kategori(id, nama.trim());
        dao.ubah(k);
    }

    public void hapusKategori(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Pilih kategori terlebih dahulu!");
        dao.hapus(id);
    }

    public List<Kategori> semuaKategori() {
        return dao.getAll();
    }
}
