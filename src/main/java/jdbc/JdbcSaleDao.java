// src/main/java/jdbc/JdbcSaleDao.java
package jdbc;

import dao.SaleDao;
import model.Sale;
import util.Db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSaleDao implements SaleDao {

    private Sale map(ResultSet rs) throws SQLException {
        Sale s = new Sale();
        s.setSaleId(rs.getInt("sale_id"));
        s.setVehicleId(rs.getInt("vehicle_id"));
        s.setSalespersonId(rs.getInt("salesperson_id"));

        int prospectId = rs.getInt("prospect_id");
        s.setProspectId(rs.wasNull() ? null : prospectId);

        Date d = rs.getDate("sale_date");
        s.setSaleDate(d != null ? d.toLocalDate() : null);

        s.setSalePrice(rs.getDouble("sale_price"));
        double comm = rs.getDouble("commission_earned");
        s.setCommissionEarned(rs.wasNull() ? null : comm);
        return s;
    }

    @Override
    public int create(Sale sale) {
        String sql = """
            INSERT INTO Sales (vehicle_id, salesperson_id, prospect_id, sale_date, sale_price, commission_earned)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sale.getVehicleId());
            ps.setInt(2, sale.getSalespersonId());

            if (sale.getProspectId() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, sale.getProspectId());

            ps.setDate(4, Date.valueOf(sale.getSaleDate()));
            ps.setDouble(5, sale.getSalePrice());

            if (sale.getCommissionEarned() == null) ps.setNull(6, Types.DECIMAL);
            else ps.setDouble(6, sale.getCommissionEarned());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating sale", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Sales WHERE sale_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting sale " + id, e);
        }
    }

    @Override
    public Optional<Sale> findById(int id) {
        String sql = "SELECT * FROM Sales WHERE sale_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding sale " + id, e);
        }
    }

    @Override
    public List<Sale> findAll() {
        String sql = "SELECT * FROM Sales ORDER BY sale_date DESC, sale_id DESC";
        List<Sale> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing sales", e);
        }
    }

    @Override
    public List<Sale> findBySalesperson(int salespersonId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT * FROM Sales
             WHERE salesperson_id = ?
               AND sale_date BETWEEN ? AND ?
             ORDER BY sale_date DESC, sale_id DESC
        """;
        List<Sale> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, salespersonId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing sales for salesperson " + salespersonId, e);
        }
    }

    @Override
    public List<Sale> findByDateRange(LocalDate from, LocalDate to) {
        String sql = """
            SELECT * FROM Sales
             WHERE sale_date BETWEEN ? AND ?
             ORDER BY sale_date DESC, sale_id DESC
        """;
        List<Sale> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing sales by date range", e);
        }
    }
}
