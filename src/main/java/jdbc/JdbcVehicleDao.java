/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import dao.VehicleDao;
import model.Vehicle;
import util.Db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public class JdbcVehicleDao implements VehicleDao {

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        Vehicle v = new Vehicle();
        v.setVehicleId(rs.getInt("vehicle_id"));
        v.setMake(rs.getString("make"));
        v.setModel(rs.getString("model"));
        v.setYear(rs.getInt("year"));
        v.setColor(rs.getString("color"));
        v.setCategory(rs.getString("category"));
        v.setCost(rs.getDouble("cost"));
        v.setPrice(rs.getDouble("price"));
        v.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("date_added");
        v.setDateAdded(ts != null ? ts.toLocalDateTime() : null);
        return v;
    }

    @Override
    public int create(Vehicle v) {
        String sql = """
            INSERT INTO Vehicles (make, model, year, color, category, cost, price, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getMake());
            ps.setString(2, v.getModel());
            ps.setInt(3, v.getYear());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getCategory());
            ps.setDouble(6, v.getCost());
            ps.setDouble(7, v.getPrice());
            ps.setString(8, v.getStatus() == null ? "Available" : v.getStatus());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting vehicle", e);
        }
    }

    @Override
    public boolean update(Vehicle v) {
        String sql = """
            UPDATE Vehicles
               SET make=?, model=?, year=?, color=?, category=?, cost=?, price=?, status=?
             WHERE vehicle_id=?
        """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMake());
            ps.setString(2, v.getModel());
            ps.setInt(3, v.getYear());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getCategory());
            ps.setDouble(6, v.getCost());
            ps.setDouble(7, v.getPrice());
            ps.setString(8, v.getStatus());
            ps.setInt(9, v.getVehicleId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vehicle id=" + v.getVehicleId(), e);
        }
    }

    @Override
    public boolean deleteById(int vehicleId) {
        String sql = "DELETE FROM Vehicles WHERE vehicle_id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle id=" + vehicleId, e);
        }
    }

    @Override
    public Optional<Vehicle> findById(int vehicleId) {
        String sql = "SELECT * FROM Vehicles WHERE vehicle_id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicle id=" + vehicleId, e);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        String sql = "SELECT * FROM Vehicles ORDER BY date_added DESC, vehicle_id DESC";
        List<Vehicle> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing vehicles", e);
        }
    }

    @Override
    public List<Vehicle> findByStatus(String status) {
        String sql = "SELECT * FROM Vehicles WHERE status=? ORDER BY date_added DESC";
        List<Vehicle> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error filtering vehicles by status=" + status, e);
        }
    }

    @Override
    public List<Vehicle> search(String make, String model, Integer year, String color) {
        StringBuilder sb = new StringBuilder("SELECT * FROM Vehicles WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (make != null && !make.isBlank()) { sb.append(" AND make LIKE ?"); params.add("%" + make + "%"); }
        if (model != null && !model.isBlank()) { sb.append(" AND model LIKE ?"); params.add("%" + model + "%"); }
        if (year != null) { sb.append(" AND year = ?"); params.add(year); }
        if (color != null && !color.isBlank()) { sb.append(" AND color LIKE ?"); params.add("%" + color + "%"); }
        sb.append(" ORDER BY price ASC, year DESC");

        List<Vehicle> results = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException("Error searching vehicles", e);
        }
    }
}
