package com.raj.AnimalMovements.model;

import java.util.Date;

public class MovementDetailsDTO {

    private Long movementId;
    private String newOriginCity;
    private String newOriginState;
    private String newDestinationName;
    private String newDestinationPostalCode;
    private String newDestinationAddress;
    private String newDestinationPremId;
    private Date newShipmentsStartDate;
    private String newOriginAddress;
    private String newMovementReason;
    private String newDestinationState;
    private String newSpecies;
    private String newOriginPostalCode;
    private Integer newNumItemsMoved;
    private String newDestinationCity;
    private String accountCompany;
    private String newOriginName;
    private String newOriginPremId;
    private Double originLon;
    private Double destinationLat;
    private Double destinationLong;
    private Double originLat;

    // Constructor
    public MovementDetailsDTO(Long movementId, String newOriginCity, String newOriginState, String newDestinationName, 
                              String newDestinationPostalCode, String newDestinationAddress, String newDestinationPremId,
                              Date newShipmentsStartDate, String newOriginAddress, String newMovementReason, 
                              String newDestinationState, String newSpecies, String newOriginPostalCode, 
                              Integer newNumItemsMoved, String newDestinationCity, String accountCompany, 
                              String newOriginName, String newOriginPremId, Double originLon, 
                              Double destinationLat, Double destinationLong, Double originLat) {
        this.movementId = movementId;
        this.newOriginCity = newOriginCity;
        this.newOriginState = newOriginState;
        this.newDestinationName = newDestinationName;
        this.newDestinationPostalCode = newDestinationPostalCode;
        this.newDestinationAddress = newDestinationAddress;
        this.newDestinationPremId = newDestinationPremId;
        this.newShipmentsStartDate = newShipmentsStartDate;
        this.newOriginAddress = newOriginAddress;
        this.newMovementReason = newMovementReason;
        this.newDestinationState = newDestinationState;
        this.newSpecies = newSpecies;
        this.newOriginPostalCode = newOriginPostalCode;
        this.newNumItemsMoved = newNumItemsMoved;
        this.newDestinationCity = newDestinationCity;
        this.accountCompany = accountCompany;
        this.newOriginName = newOriginName;
        this.newOriginPremId = newOriginPremId;
        this.originLon = originLon;
        this.destinationLat = destinationLat;
        this.destinationLong = destinationLong;
        this.originLat = originLat;
    }

    // Getters
    public Long getMovementId() { return movementId; }
    public String getNewOriginCity() { return newOriginCity; }
    public String getNewOriginState() { return newOriginState; }
    public String getNewDestinationName() { return newDestinationName; }
    public String getNewDestinationPostalCode() { return newDestinationPostalCode; }
    public String getNewDestinationAddress() { return newDestinationAddress; }
    public String getNewDestinationPremId() { return newDestinationPremId; }
    public Date getNewShipmentsStartDate() { return newShipmentsStartDate; }
    public String getNewOriginAddress() { return newOriginAddress; }
    public String getNewMovementReason() { return newMovementReason; }
    public String getNewDestinationState() { return newDestinationState; }
    public String getNewSpecies() { return newSpecies; }
    public String getNewOriginPostalCode() { return newOriginPostalCode; }
    public Integer getNewNumItemsMoved() { return newNumItemsMoved; }
    public String getNewDestinationCity() { return newDestinationCity; }
    public String getAccountCompany() { return accountCompany; }
    public String getNewOriginName() { return newOriginName; }
    public String getNewOriginPremId() { return newOriginPremId; }
    public Double getOriginLon() { return originLon; }
    public Double getDestinationLat() { return destinationLat; }
    public Double getDestinationLong() { return destinationLong; }
    public Double getOriginLat() { return originLat; }
}
