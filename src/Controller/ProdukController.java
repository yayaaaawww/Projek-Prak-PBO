package Controller;

import Model.ModelSupermarket.Produk;
import Model.ModelSupermarket.ProdukDAO;
import java.util.List;

/**
 * ProdukController - Menghubungkan View dan Model untuk Produk
 */
public class ProdukController {

    private ProdukDAO dao;

    public ProdukController() {
        this.dao = new ProdukDAO();
    }

    public void tambahProduk(String nama, int idKategori, double harga, int stok) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong!");
        }
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0!");
        }
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif!");
        }
        Produk p = new Produk();
        p.setNama(nama.trim());
        p.setIdKategori(idKategori);
        p.setHarga(harga);
        p.setStok(stok);
        dao.tambah(p);
    }

    public void ubahProduk(int id, String nama, int idKategori, double harga, int stok) {
        if (id <= 0) {
            throw new IllegalArgumentException("Pilih produk terlebih dahulu!");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong!");
        }
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0!");
        }
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif!");
        }
        Produk p = new Produk();
        p.setId(id);
        p.setNama(nama.trim());
        p.setIdKategori(idKategori);
        p.setHarga(harga);
        p.setStok(stok);
        dao.ubah(p);
    }

    public void hapusProduk(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Pilih produk terlebih dahulu!");
        }
        dao.hapus(id);
    }

    public Produk getProdukById(int id) {
        return dao.cariById(id);
    }

    public List<Produk> semuaProduk() {
        return dao.getAll();
    }

    public List<Produk> cariProduk(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return dao.getAll();
        }
        return dao.cariByNama(keyword.trim());
    }
}