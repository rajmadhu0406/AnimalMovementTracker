package com.raj.AnimalMovements.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account company is required")
    @Column(name = "account_company", nullable = false)
    private String accountCompany;

    @NotBlank(message = "Movement reason is required")
    @Column(name = "new_movementreason", nullable = false)
    private String newMovementReason;

    @NotNull(message = "Number of items moved is required")
    @Column(name = "new_numitemsmoved", nullable = false)
    @Min(value = 1, message = "Number of items moved must be greater than 0")
    private Integer newNumItemsMoved;

    @NotNull(message = "Shipment start date is required")
    @Column(name = "new_shipmentsstartdate", nullable = false)
    private Date newShipmentsStartDate;

    @NotBlank(message = "Species is required")
    @Column(name = "new_species", nullable = false)
    private String newSpecies;

    // Foreign Key: Origin Farm (References Farm's premiseId)
    @ManyToOne
    @JoinColumn(name = "new_originfarm", referencedColumnName = "premise_id", nullable = false)
    private Farm newOriginFarm;

    // Foreign Key: Destination Farm (References Farm's premiseId)
    @ManyToOne
    @JoinColumn(name = "new_destinationfarm", referencedColumnName = "premise_id", nullable = false)
    private Farm newDestinationFarm;

    // Default constructor
    public Movement() {
    }

    // Parameterized constructor
    public Movement(String accountCompany, String newMovementReason, Integer newNumItemsMoved, Date newShipmentsStartDate, 
                    String newSpecies, Farm newOriginFarm, Farm newDestinationFarm) {
        this.accountCompany = accountCompany;
        this.newMovementReason = newMovementReason;
        this.newNumItemsMoved = newNumItemsMoved;
        this.newShipmentsStartDate = newShipmentsStartDate;
        this.newSpecies = newSpecies;
        this.newOriginFarm = newOriginFarm;
        this.newDestinationFarm = newDestinationFarm;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountCompany() { return accountCompany; }
    public void setAccountCompany(String accountCompany) { this.accountCompany = accountCompany; }

    public String getNewMovementReason() { return newMovementReason; }
    public void setNewMovementReason(String newMovementReason) { this.newMovementReason = newMovementReason; }

    public Integer getNewNumItemsMoved() { return newNumItemsMoved; }
    public void setNewNumItemsMoved(Integer newNumItemsMoved) { this.newNumItemsMoved = newNumItemsMoved; }

    public Date getNewShipmentsStartDate() { return newShipmentsStartDate; }
    public void setNewShipmentsStartDate(Date newShipmentsStartDate) { this.newShipmentsStartDate = newShipmentsStartDate; }

    public String getNewSpecies() { return newSpecies; }
    public void setNewSpecies(String newSpecies) { this.newSpecies = newSpecies; }

    public Farm getNewOriginFarm() { return newOriginFarm; }
    public void setNewOriginFarm(Farm newOriginFarm) { this.newOriginFarm = newOriginFarm; }

    public Farm getNewDestinationFarm() { return newDestinationFarm; }
    public void setNewDestinationFarm(Farm newDestinationFarm) { this.newDestinationFarm = newDestinationFarm; }

    @Override
    public String toString() {
        return "Movement{" +
                "id=" + id +
                ", accountCompany='" + accountCompany + '\'' +
                ", newMovementReason='" + newMovementReason + '\'' +
                ", newNumItemsMoved=" + newNumItemsMoved +
                ", newShipmentsStartDate=" + newShipmentsStartDate +
                ", newSpecies='" + newSpecies + '\'' +
                ", newOriginFarm=" + (newOriginFarm != null ? newOriginFarm.getPremiseId() : "null") +
                ", newDestinationFarm=" + (newDestinationFarm != null ? newDestinationFarm.getPremiseId() : "null") +
                '}';
    }
}
