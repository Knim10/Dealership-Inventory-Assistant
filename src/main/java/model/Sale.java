/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
/**
 *
 * @author knim1
 */
public class Sale {
    private Integer saleId;
    private Integer vehicleId;
    private Integer salespersonId;
    private Integer prospectId; // nullable
    private LocalDate saleDate;
    private Double salePrice;
    private Double commissionEarned;

    public Integer getSaleId() { return saleId; }
    public void setSaleId(Integer saleId) { this.saleId = saleId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public Integer getSalespersonId() { return salespersonId; }
    public void setSalespersonId(Integer salespersonId) { this.salespersonId = salespersonId; }

    public Integer getProspectId() { return prospectId; }
    public void setProspectId(Integer prospectId) { this.prospectId = prospectId; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }

    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }

    public Double getCommissionEarned() { return commissionEarned; }
    public void setCommissionEarned(Double commissionEarned) { this.commissionEarned = commissionEarned; }
}
