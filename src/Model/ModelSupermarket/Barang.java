/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

/**
 *
 * @author LENOVO
 */


public class Barang {

    protected int id;
    protected String nama;
    protected double harga;

    public Barang() {}

    public Barang(int id, String nama, double harga) {
        this.id    = id;
        this.nama  = nama;
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
    

    @Override
    public String toString() {
        return "Barang{id=" + id + ", nama=" + nama + ", harga=" + harga + "}";
    }
}