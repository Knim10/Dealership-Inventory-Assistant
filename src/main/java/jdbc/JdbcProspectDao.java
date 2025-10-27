/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import dao.ProspectDao;
import model.Prospect;
import util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public class JdbcProspectDao implements ProspectDao {
    private Prospect map(ResultSet rs) throws SQLException {
        Prospect p = new Prospect();
        p.setProspectId(rs.getInt("prospect_id"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setEmail(rs.getString("email"));
        p.setPhone(rs.getString("phone"));
        p.setDesiredMake(rs.getString("desired_make"));
        p.setDesiredModel(rs.getString("desired_model"));
        p.setDesiredColor(rs.getString("desired_color"));
        Timestamp ts = rs.getTimestamp("created_at");
        p.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);
        return p;
    }

    @Override
    public int create(Prospect p) {
        String sql = """
            INSERT INTO Prospects (first_name, last_name, email, phone, desired_make, desired_model, desired_color)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getPhone());
            ps.setString(5, p.getDesiredMake());
            ps.setString(6, p.getDesiredModel());
            ps.setString(7, p.getDesiredColor());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting prospect", e);
        }
    }

    @Override public boolean update(Prospect p) { /* implement like Vehicle */ return false; }
    @Override public boolean deleteById(int id) { /* implement like Vehicle */ return false; }
    @Override public Optional<Prospect> findById(int id) { /* implement like Vehicle */ return Optional.empty(); }

    @Override
    public List<Prospect> findAll() {
        String sql = "SELECT * FROM Prospects ORDER BY created_at DESC, prospect_id DESC";
        List<Prospect> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing prospects", e);
        }
    }
}
