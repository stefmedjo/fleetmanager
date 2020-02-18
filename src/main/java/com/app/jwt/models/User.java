package com.app.jwt.models;

import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class User extends PanacheEntity{

    @NotBlank
    @Column(unique = true)
    public String username;
    
    public String fname;
    public String lname;
    
    @JsonIgnore
    @JsonbTransient
    @NotBlank
    public String password;
    
    public String role;

    public boolean isEnabled = true;

    public String salt;

    public String post;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("assignedTo")
    public List<Vehicle> assignedVehicles;

    @OneToMany(mappedBy="drivedBy", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("drivedBy")
    public List<Vehicle> drivedVehicles;


    public static User findByUsername(String username){
        return find("username = ?1 and isEnabled = ?2",username, true).firstResult();
    }

}