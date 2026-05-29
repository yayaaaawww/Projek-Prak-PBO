/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ModelSupermarket;

import java.util.List;

/**
 *
 * @author LENOVO
 */
public interface Manageable<T> {
    void tambah(T obj);
    void ubah(T obj);
    void hapus(int id);
    T cariById(int id);
    List<T> getAll();
}
