/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import dao.SalespersonDao;
import model.Salesperson;
import util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public class JdbcSalespersonDao implements SalespersonDao {
    private Salesperson map(ResultSet rs) throws SQLException {
        Salesperson s = new Salesperson();
        s.setSalespersonId(rs.getInt("salesperson_id"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setCommissionRate(rs.getDouble("commission_rate"));
        return s;
    }

    @Override
    public int create(Salesperson s) {
    String sql = """
        INSERT INTO Salespersons (first_name, last_name, email, phone, commission_rate)
        VALUES (?, ?, ?, ?, ?)
    """;
    try (Connection c = Db.getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, s.getFirstName());
        ps.setString(2, s.getLastName());
        ps.setString(3, s.getEmail());
        ps.setString(4, s.getPhone());
        ps.setDouble(5, s.getCommissionRate());

        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            return keys.next() ? keys.getInt(1) : -1;
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error creating salesperson", e);
    }
    }

    @Override public boolean update(Salesperson s) { /* implement UPDATE */ return false; }
    @Override public boolean deleteById(int id) { /* implement DELETE */ return false; }

    @Override
    public Optional<Salesperson> findById(int id) {
        String sql = "SELECT * FROM Salespersons WHERE salesperson_id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error find salesperson id=" + id, e);
        }
    }

    @Override
    public Optional<Salesperson> findByEmail(String email) {
        String sql = "SELECT * FROM Salespersons WHERE email=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error find salesperson email=" + email, e);
        }
    }

    @Override
    public List<Salesperson> findAll() {
        String sql = "SELECT * FROM Salespersons ORDER BY last_name, first_name";
        List<Salesperson> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing salespersons", e);
        }
    }
}
