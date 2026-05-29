/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

import Model.Connector;
import java.sql.*;
/**
 *
 * @author LENOVO
 */


public class UserDAO {

    private Connection conn;

    public UserDAO() {
        this.conn = Connector.getConnection();
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("nama_lengkap")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error login: " + e.getMessage());
        }
        return null; // null = login gagal
    }
}