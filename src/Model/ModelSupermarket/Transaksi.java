/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

/**
 *
 * @author LENOVO
 */

import java.util.Date;

public class Transaksi {

    private int idTransaksi;
    private Date tanggal;
    private double total;

    public Transaksi() {}

    public Transaksi(int idTransaksi, Date tanggal, double total) {
        this.idTransaksi = idTransaksi;
        this.tanggal     = tanggal;
        this.total       = total;
    }

    // Getter & Setter
    public int getIdTransaksi()                    { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi)    { this.idTransaksi = idTransaksi; }

    public Date getTanggal()                       { return tanggal; }
    public void setTanggal(Date tanggal)           { this.tanggal = tanggal; }

    public double getTotal()                       { return total; }
    public void setTotal(double total)             { this.total = total; }

    @Override
    public String toString() {
        return "Transaksi{id=" + idTransaksi + ", tanggal=" + tanggal + ", total=" + total + "}";
    }
}