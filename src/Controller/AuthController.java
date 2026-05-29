/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ModelSupermarket.User;
import Model.ModelSupermarket.UserDAO;
/**
 *
 * @author LENOVO
 */
public class AuthController {

    private UserDAO dao;

    public AuthController() {
        this.dao = new UserDAO();
    }

    /**
     * Login: return User jika berhasil, null jika gagal
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong!");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong!");
        }
        return dao.login(username.trim(), password);
    }
}
