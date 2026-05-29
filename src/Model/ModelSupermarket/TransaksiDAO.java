/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

import Model.Connector;
import java.sql.*;
import java.util.*;

/**
 *
 * @author LENOVO
 */


public class TransaksiDAO {

    private Connection conn;

    public TransaksiDAO() {
        this.conn = Connector.getConnection();
    }

    public int simpanTransaksi(List<DetailTransaksi> items) throws SQLException {
        conn.setAutoCommit(false);
        try {
            double total = items.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();

            int idTransaksi;
            String sqlHeader = "INSERT INTO transaksi (total) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, total);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                idTransaksi = rs.getInt(1);
            }

            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, subtotal) VALUES (?,?,?,?)";
            String sqlStok   = "UPDATE produk SET stok = stok - ? WHERE id_produk = ? AND stok >= ?";

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement psStok   = conn.prepareStatement(sqlStok)) {
                for (DetailTransaksi d : items) {
                    psStok.setInt(1, d.getJumlah());
                    psStok.setInt(2, d.getIdProduk());
                    psStok.setInt(3, d.getJumlah());
                    int updated = psStok.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException("Stok \"" + d.getNamaProduk() + "\" tidak mencukupi!");
                    }
                    psDetail.setInt(1, idTransaksi);
                    psDetail.setInt(2, d.getIdProduk());
                    psDetail.setInt(3, d.getJumlah());
                    psDetail.setDouble(4, d.getSubtotal());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            return idTransaksi;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Transaksi> getAll() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY tanggal DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Transaksi(
                    rs.getInt("id_transaksi"),
                    rs.getTimestamp("tanggal"),
                    rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getAll transaksi: " + e.getMessage());
        }
        return list;
    }

    public List<DetailTransaksi> getDetailByTransaksi(int idTransaksi) {
        List<DetailTransaksi> list = new ArrayList<>();
        String sql = "SELECT dt.*, p.nama_produk, p.harga FROM detail_transaksi dt "
                   + "JOIN produk p ON dt.id_produk = p.id_produk "
                   + "WHERE dt.id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DetailTransaksi(
                    rs.getInt("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getInt("jumlah"),
                    rs.getDouble("harga")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getDetail: " + e.getMessage());
        }
        return list;
    }
}
