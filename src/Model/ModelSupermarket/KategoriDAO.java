/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

import Model.Connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author LENOVO
 */


public class KategoriDAO implements Manageable<Kategori> {

    private Connection conn;

    public KategoriDAO() {
        this.conn = Connector.getConnection();
    }

    @Override
    public void tambah(Kategori k) {
        String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKategori());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error tambah kategori: " + e.getMessage());
        }
    }

    @Override
    public void ubah(Kategori k) {
        String sql = "UPDATE kategori SET nama_kategori=? WHERE id_kategori=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKategori());
            ps.setInt(2, k.getIdKategori());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error ubah kategori: " + e.getMessage());
        }
    }

    @Override
    public void hapus(int id) {
        String sql = "DELETE FROM kategori WHERE id_kategori=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error hapus kategori: " + e.getMessage());
        }
    }

    @Override
    public Kategori cariById(int id) {
        String sql = "SELECT * FROM kategori WHERE id_kategori=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("Error cari kategori: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Kategori> getAll() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY id_kategori";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error getAll kategori: " + e.getMessage());
        }
        return list;
    }

    private Kategori mapRow(ResultSet rs) throws SQLException {
        return new Kategori(rs.getInt("id_kategori"), rs.getString("nama_kategori"));
    }
}
