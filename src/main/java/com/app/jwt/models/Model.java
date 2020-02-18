package com.app.jwt.models;

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
public class Model extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String code;

    @NotBlank
    public String designation;

    @ManyToOne
    @JsonIgnoreProperties("models")
    public Brand brand;

    @OneToMany(mappedBy = "model", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("model")
    public List<Vehicle> vehicles;
    
}