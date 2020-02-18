package com.app.jwt.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Category extends PanacheEntity {

    @NotBlank
    @Column(nullable = false)
    public String code;

    @NotBlank
    public String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("category")
    public List<Vehicle> vehicles;
}