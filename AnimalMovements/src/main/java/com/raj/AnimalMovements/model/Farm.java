package com.raj.AnimalMovements.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "farms")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Premise ID is required")
    @Column(name = "premise_id", nullable = false, unique = true)
    private String premiseId;

    @NotNull(message = "Total animals is required")
    @Min(value = 0, message = "Total animals must be a positive number")
    @Column(name = "total_animal", nullable = false)
    private int totalAnimal;

    // Default constructor (required by JPA)
    public Farm() {
    }

    // Parameterized constructor
    public Farm(String premiseId, int totalAnimal) {
        this.premiseId = premiseId;
        this.totalAnimal = totalAnimal;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPremiseId() {
        return premiseId;
    }

    public void setPremiseId(String premiseId) {
        this.premiseId = premiseId;
    }

    public int getTotalAnimal() {
        return totalAnimal;
    }

    public void setTotalAnimal(int totalAnimal) {
        this.totalAnimal = totalAnimal;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", premiseId='" + premiseId + '\'' +
                ", totalAnimal=" + totalAnimal +
                '}';
    }
}