/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

/**
 *
 * @author LENOVO
 */
public class DetailTransaksi {
    private int idProduk;
    private String namaProduk;
    private int jumlah;
    private double hargaSatuan;
    private double subtotal;

    public DetailTransaksi() {}

    public DetailTransaksi(int idProduk, String namaProduk, int jumlah, double hargaSatuan) {
        this.idProduk    = idProduk;
        this.namaProduk  = namaProduk;
        this.jumlah      = jumlah;
        this.hargaSatuan = hargaSatuan;
        this.subtotal    = jumlah * hargaSatuan;
    }

    public int getIdProduk()                    { return idProduk; }
    public void setIdProduk(int idProduk)       { this.idProduk = idProduk; }

    public String getNamaProduk()               { return namaProduk; }
    public void setNamaProduk(String n)         { this.namaProduk = n; }

    public int getJumlah()                      { return jumlah; }
    public void setJumlah(int jumlah) {
        this.jumlah   = jumlah;
        this.subtotal = jumlah * hargaSatuan;
    }

    public double getHargaSatuan()              { return hargaSatuan; }
    public void setHargaSatuan(double h) {
        this.hargaSatuan = h;
        this.subtotal    = jumlah * h;
    }

    public double getSubtotal()                 { return subtotal; }
}
