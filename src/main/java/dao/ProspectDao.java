/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Prospect;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public interface ProspectDao {
    int create(Prospect p);
    boolean update(Prospect p);
    boolean deleteById(int id);

    Optional<Prospect> findById(int id);
    List<Prospect> findAll();
}
