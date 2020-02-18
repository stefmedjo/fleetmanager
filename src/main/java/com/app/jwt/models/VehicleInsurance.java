package com.app.jwt.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class VehicleInsurance extends PanacheEntity{

    public String code;
    
    @ManyToOne
    public Vehicle vehicle;

    @ManyToOne
    public Insurance insurance;

    public LocalDate startDate;
    public LocalDate endDate;

}