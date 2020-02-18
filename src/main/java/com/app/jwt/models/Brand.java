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
public class Brand extends PanacheEntity{

    @NotBlank
    @Column(unique = true)
    public String code;

    @NotBlank
    public String designation;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("brand")
    public List<Model> models;
}