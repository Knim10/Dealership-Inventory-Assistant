/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Salesperson;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public interface SalespersonDao {
    int create(Salesperson s);
    boolean update(Salesperson s);
    boolean deleteById(int id);

    Optional<Salesperson> findById(int id);
    Optional<Salesperson> findByEmail(String email);
    List<Salesperson> findAll();
}
