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
public class Prospect {
    private Integer prospectId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String desiredMake;
    private String desiredModel;
    private String desiredColor;
    private LocalDateTime createdAt;

    public Integer getProspectId() { return prospectId; }
    public void setProspectId(Integer prospectId) { this.prospectId = prospectId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDesiredMake() { return desiredMake; }
    public void setDesiredMake(String desiredMake) { this.desiredMake = desiredMake; }

    public String getDesiredModel() { return desiredModel; }
    public void setDesiredModel(String desiredModel) { this.desiredModel = desiredModel; }

    public String getDesiredColor() { return desiredColor; }
    public void setDesiredColor(String desiredColor) { this.desiredColor = desiredColor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
