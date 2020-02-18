package com.app.jwt.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Measurement extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String code;

    @CreationTimestamp
    public LocalDate createdOn;
    
    @NotBlank
    public Double fuelConsumption = 0.0;
    
    //public Double kilometrage;

    @ManyToOne
    @JsonIgnoreProperties("measurements")
    public Vehicle vehicle;

}