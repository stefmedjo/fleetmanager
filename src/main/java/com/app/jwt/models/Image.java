package com.app.jwt.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Image extends PanacheEntity {

    public String path;
    public boolean isDefault = true;

    @ManyToOne
    @JsonIgnoreProperties("images")
    public Vehicle vehicle;
    
}