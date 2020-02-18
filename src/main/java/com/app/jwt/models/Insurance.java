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
public class Insurance extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String code;
    public String designation;
    public String description;

    @ManyToOne
    public Supplier supplier;

    @JsonIgnoreProperties("insurance")
    @OneToMany(mappedBy = "insurance", fetch = FetchType.EAGER)
    public List<VehicleInsurance> vehicleInsurances;

}