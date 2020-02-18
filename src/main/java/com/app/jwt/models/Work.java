package com.app.jwt.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Work extends PanacheEntity{

    @NotBlank
    @Column(unique = true)
    public String code;

    @NotBlank
    public String designation;

    public String description;

    public LocalDateTime realizedOn;

    public LocalDateTime expectedOn;

    @CreationTimestamp
    public LocalDateTime createdOn;

    @ManyToOne
    public Vehicle vehicle;
    
}