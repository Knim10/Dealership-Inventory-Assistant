/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import dao.UserDao;
import model.User;
import util.Db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

/**
 *
 * @author knim1
 */
public class JdbcUserDao implements UserDao {
    
    private static final Logger log = Logger.getLogger(JdbcUserDao.class.getName());
    
    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        int spId = rs.getInt("salesperson_id");
        u.setSalespersonId(rs.wasNull() ? null : spId);
        Timestamp ts = rs.getTimestamp("created_at");
        u.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);
        return u;
    }


@Override
public Optional<User> findByUsername(String username) {
    String sql = "SELECT * FROM Users WHERE username = ?";
    try (Connection c = Db.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        String catalog = "(unknown)";
    try {
    catalog = c.getCatalog();
    } catch (SQLException ignored) { }

        log.info("[DEBUG DB] Connected to schema: " + catalog);


        ps.setString(1, username);

        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                log.info(() -> "[DEBUG DB] No user found: " + username);
                return Optional.empty();
            }

            User u = map(rs);
            log.info(() -> "[DEBUG DB] Loaded user from DB: username=" + u.getUsername()
                    + ", role=" + u.getRole()
                    + ", salespersonId=" + u.getSalespersonId());

            return Optional.of(u);
        }

    } catch (SQLException e) {
        log.severe("Error finding user: " + e.getMessage());
        throw new RuntimeException("Error finding user", e);
    }
}


@Override
public int create(User u) {
    String sql = """
        INSERT INTO Users (username, password_hash, role, salesperson_id)
        VALUES (?, ?, ?, ?)
    """;
    try (Connection c = Db.getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPasswordHash());
        ps.setString(3, u.getRole());

        if (u.getSalespersonId() == null) ps.setNull(4, Types.INTEGER);
        else ps.setInt(4, u.getSalespersonId());

        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            return keys.next() ? keys.getInt(1) : -1;
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error creating user", e);
    }
}

}
