/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.User;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public interface UserDao {
    int create(User u);
    Optional<User> findByUsername(String username);
}
