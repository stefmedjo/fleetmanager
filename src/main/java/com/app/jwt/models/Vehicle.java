package com.app.jwt.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Vehicle extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String code;

    public String designation;

    public String description;
    
    public int builtOn;

    public LocalDate purchasedOn;

    @ManyToOne
    @JsonIgnoreProperties("vehicles")
    public Model model;

    @ManyToOne
    @JsonIgnoreProperties("vehicles")
    public User assignedTo;

    @ManyToOne
    @JsonIgnoreProperties("vehicles")
    public User drivedBy;

    @JsonIgnoreProperties("vehicle")
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    public List<Work> works;

    @JsonIgnoreProperties("vehicle")
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    public List<Measurement> measurements;

    @JsonIgnoreProperties("vehicle")
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    public List<VehicleInsurance> insurances;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vehicle")
    public List<Image> images;
}