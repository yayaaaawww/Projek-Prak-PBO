/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

/**
 *
 * @author LENOVO
 */

public class Produk extends Barang {

    private int idKategori;
    private String namaKategori;
    private int stok;

    public Produk() {}

    public Produk(int id, String nama, int idKategori, String namaKategori, double harga, int stok) {
        super(id, nama, harga);
        this.idKategori    = idKategori;
        this.namaKategori  = namaKategori;
        this.stok          = stok;
    }

    // Getter & Setter
    public int getIdKategori()                      { return idKategori; }
    public void setIdKategori(int idKategori)        { this.idKategori = idKategori; }

    public String getNamaKategori()                  { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

    public int getStok()                             { return stok; }
    public void setStok(int stok)                    { this.stok = stok; }

    @Override
    public String toString() {
        return "Produk{id=" + id + ", nama=" + nama + ", kategori=" + namaKategori
               + ", harga=" + harga + ", stok=" + stok + "}";
    }
}

