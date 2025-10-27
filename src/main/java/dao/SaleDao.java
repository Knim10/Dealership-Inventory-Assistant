/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Sale;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public interface SaleDao {
    int create(Sale sale);
    boolean deleteById(int id);

    Optional<Sale> findById(int id);
    List<Sale> findAll();
    List<Sale> findBySalesperson(int salespersonId, LocalDate from, LocalDate to);
}
