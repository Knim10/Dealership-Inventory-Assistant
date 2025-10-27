/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import dao.UserDao;
import model.User;

import java.util.Optional;

/**
 *
 * @author knim1
 */
public class JdbcUserDao implements UserDao {
    @Override public int create(User u) { /* INSERT Users with BCrypt hash */ return -1; }
    @Override public Optional<User> findByUsername(String username) { /* SELECT by username */ return Optional.empty(); }
}
