/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import dao.SaleDao;
import model.Sale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public class JdbcSaleDao implements SaleDao {
    @Override public int create(Sale sale) { /* INSERT Sales */ return -1; }
    @Override public boolean deleteById(int id) { /* DELETE */ return false; }
    @Override public Optional<Sale> findById(int id) { /* SELECT */ return Optional.empty(); }
    @Override public List<Sale> findAll() { /* SELECT * */ return List.of(); }
    @Override public List<Sale> findBySalesperson(int salespersonId, LocalDate from, LocalDate to) { /* date range */ return List.of(); }
}
