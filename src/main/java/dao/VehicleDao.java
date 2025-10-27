/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Vehicle;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author knim1
 */
public interface VehicleDao {
    int create(Vehicle v);
    boolean update(Vehicle v);
    boolean deleteById(int vehicleId);

    Optional<Vehicle> findById(int vehicleId);
    List<Vehicle> findAll();
    List<Vehicle> findByStatus(String status);     // e.g., "Available"
    List<Vehicle> search(String make, String model, Integer year, String color);
}
