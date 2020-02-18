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
public class Supplier extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String code;

    @NotBlank
    public String name;

    public String phone;

    public String address;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("supplier")
    public List<Insurance> insurances;

}