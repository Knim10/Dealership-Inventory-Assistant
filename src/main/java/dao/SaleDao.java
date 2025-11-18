// src/main/java/dao/SaleDao.java
package dao;

import model.Sale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaleDao {
    int create(Sale sale);
    boolean deleteById(int id);

    Optional<Sale> findById(int id);
    List<Sale> findAll();

    // All sales for a single salesperson between dates (inclusive)
    List<Sale> findBySalesperson(int salespersonId, LocalDate from, LocalDate to);

    // All sales between dates (inclusive), any salesperson
    List<Sale> findByDateRange(LocalDate from, LocalDate to);
}
