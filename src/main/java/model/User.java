/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
/**
 *
 * @author knim1
 */
public class User {
    private Integer userId;
    private String username;
    private String passwordHash; // store BCrypt hash
    private String role;         // 'admin' or 'sales'
    private LocalDateTime createdAt;
    private Integer salespersonId;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getSalespersonId() { return salespersonId; }
    public void setSalespersonId(Integer salespersonId) { this.salespersonId = salespersonId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
