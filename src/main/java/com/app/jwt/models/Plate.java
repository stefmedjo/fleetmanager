package com.app.jwt.models;

import java.time.LocalDate;

import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class Plate extends PanacheEntity{

    public String value;
    public LocalDate expirationDate;
    public boolean isCurrent;
    
    @ManyToOne
    @JsonIgnoreProperties("plates")
    Vehicle vehicle;

}