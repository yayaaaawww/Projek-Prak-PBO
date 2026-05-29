/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

/**
 *
 * @author LENOVO
 */
public class Kategori {
    private int idKategori;
    private String namaKategori;

    public Kategori() {}

    public Kategori(int idKategori, String namaKategori) {
        this.idKategori   = idKategori;
        this.namaKategori = namaKategori;
    }

    public int getIdKategori()                          { return idKategori; }
    public void setIdKategori(int idKategori)           { this.idKategori = idKategori; }

    public String getNamaKategori()                     { return namaKategori; }
    public void setNamaKategori(String namaKategori)    { this.namaKategori = namaKategori; }

    @Override
    public String toString() {
        return namaKategori; // supaya bisa langsung tampil di JComboBox
    }
}
