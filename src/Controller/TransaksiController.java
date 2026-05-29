/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ModelSupermarket.DetailTransaksi;
import Model.ModelSupermarket.TransaksiDAO;
import Model.ModelSupermarket.Transaksi;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author LENOVO
 */


public class TransaksiController {

    private TransaksiDAO dao;

    public TransaksiController() {
        this.dao = new TransaksiDAO();
    }

    public int prosesTransaksi(List<DetailTransaksi> keranjang) throws SQLException {
        if (keranjang == null || keranjang.isEmpty()) {
            throw new IllegalArgumentException("Keranjang belanja kosong!");
        }
        return dao.simpanTransaksi(keranjang);
    }

    public List<Transaksi> semuaTransaksi() {
        return dao.getAll();
    }

    public List<DetailTransaksi> detailTransaksi(int idTransaksi) {
        return dao.getDetailByTransaksi(idTransaksi);
    }

    public double hitungTotal(List<DetailTransaksi> keranjang) {
        return keranjang.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();
    }
}