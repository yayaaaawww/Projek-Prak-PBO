/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

/**
 *
 * @author LENOVO
 */

import Model.Connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO implements Manageable<Produk> {

    private Connection conn;

    public ProdukDAO() {
        this.conn = Connector.getConnection();
    }

    @Override
    public void tambah(Produk p) {
        String sql = "INSERT INTO produk (nama_produk, id_kategori, harga, stok) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setInt(2, p.getIdKategori());
            ps.setDouble(3, p.getHarga());
            ps.setInt(4, p.getStok());
            ps.executeUpdate();
            System.out.println("Produk berhasil ditambahkan.");
        } catch (SQLException e) {
            System.err.println("Error tambah produk: " + e.getMessage());
        }
    }

    @Override
    public void ubah(Produk p) {
        String sql = "UPDATE produk SET nama_produk=?, id_kategori=?, harga=?, stok=? WHERE id_produk=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setInt(2, p.getIdKategori());
            ps.setDouble(3, p.getHarga());
            ps.setInt(4, p.getStok());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
            System.out.println("Produk berhasil diubah.");
        } catch (SQLException e) {
            System.err.println("Error ubah produk: " + e.getMessage());
        }
    }

    @Override
    public void hapus(int id) {
        String sql = "DELETE FROM produk WHERE id_produk=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Produk berhasil dihapus.");
        } catch (SQLException e) {
            System.err.println("Error hapus produk: " + e.getMessage());
        }
    }

    @Override
    public Produk cariById(int id) {
        String sql = "SELECT p.*, k.nama_kategori FROM produk p "
                   + "LEFT JOIN kategori k ON p.id_kategori = k.id_kategori "
                   + "WHERE p.id_produk = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error cari produk: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Produk> getAll() {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT p.*, k.nama_kategori FROM produk p "
                   + "LEFT JOIN kategori k ON p.id_kategori = k.id_kategori "
                   + "ORDER BY p.id_produk";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAll produk: " + e.getMessage());
        }
        return list;
    }

    public List<Produk> cariByNama(String keyword) {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT p.*, k.nama_kategori FROM produk p "
                   + "LEFT JOIN kategori k ON p.id_kategori = k.id_kategori "
                   + "WHERE p.nama_produk LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error cari nama produk: " + e.getMessage());
        }
        return list;
    }

    private Produk mapRow(ResultSet rs) throws SQLException {
        return new Produk(
            rs.getInt("id_produk"),
            rs.getString("nama_produk"),
            rs.getInt("id_kategori"),
            rs.getString("nama_kategori"),
            rs.getDouble("harga"),
            rs.getInt("stok")
        );
    }
}
