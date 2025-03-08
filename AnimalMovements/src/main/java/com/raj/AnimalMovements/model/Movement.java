package com.raj.AnimalMovements.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_company", nullable = false)
    @NotBlank(message = "accountCompany is required")
    private String accountCompany;

    @Column(name = "new_movementreason", nullable = false)
    @NotBlank(message = "newMovementReason is required")
    private String newMovementReason;

    @Column(name = "new_species", nullable = false)
    @NotBlank(message = "newSpecies is required")
    private String newSpecies;

    @Column(name = "new_originaddress", nullable = false)
    @NotBlank(message = "newOriginAddress is required")
    private String newOriginAddress;

    @Column(name = "new_origincity", nullable = false)
    @NotBlank(message = "newOriginCity is required")
    private String newOriginCity;

    @Column(name = "new_originname", nullable = false)
    @NotBlank(message = "newOriginName is required")
    private String newOriginName;

    @Column(name = "new_originpostalcode", nullable = false)
    @NotBlank(message = "newOriginPostalCode is required")
    private String newOriginPostalCode;

    @Column(name = "new_originpremid", nullable = false)
    @NotBlank(message = "newOriginPremId is required")
    private String newOriginPremId;

    @Column(name = "new_originstate", nullable = false)
    @NotBlank(message = "newOriginState is required")
    private String newOriginState;

    @Column(name = "new_destinationaddress", nullable = false)
    @NotBlank(message = "newDestinationAddress is required")
    private String newDestinationAddress;

    @Column(name = "new_destinationcity", nullable = false)
    @NotBlank(message = "newDestinationCity is required")
    private String newDestinationCity;

    @Column(name = "new_destinationname", nullable = false)
    @NotBlank(message = "newDestinationName is required")
    private String newDestinationName;

    @Column(name = "new_destinationpostalcode", nullable = false)
    @NotBlank(message = "newDestinationPostalCode is required")
    private String newDestinationPostalCode;

    @Column(name = "new_destinationpremid", nullable = false)
    @NotBlank(message = "newDestinationPremId is required")
    private String newDestinationPremId;

    @Column(name = "new_destinationstate", nullable = false)
    @NotBlank(message = "newDestinationState is required")
    private String newDestinationState;

    @Column(name = "origin_lat", nullable = false)
    @NotNull(message = "originLat is required")
    private Double originLat;

    @Column(name = "origin_lon", nullable = false)
    @NotNull(message = "originLon is required")
    private Double originLon;

    @Column(name = "destination_lat", nullable = false)
    @NotNull(message = "destinationLat is required")
    private Double destinationLat;

    @Column(name = "destination_long", nullable = false)
    @NotNull(message = "destinationLong is required")
    private Double destinationLong;

    @Column(name = "new_numitemsmoved", nullable = false)
    @NotNull(message = "newNumItemsMoved is required")
    private Integer newNumItemsMoved;

    @Column(name = "new_shipmentsstartdate", nullable = false)
    @NotNull(message = "newShipmentsStartDate is required")
    private Date newShipmentsStartDate;

    // Default constructor (required by JPA)
    public Movement() {
    }

    // Parameterized constructor
    public Movement(String accountCompany, String newMovementReason, String newSpecies, String newOriginAddress,
                    String newOriginCity, String newOriginName, String newOriginPostalCode, String newOriginPremId,
                    String newOriginState, String newDestinationAddress, String newDestinationCity,
                    String newDestinationName, String newDestinationPostalCode, String newDestinationPremId,
                    String newDestinationState, Double originLat, Double originLon, Double destinationLat,
                    Double destinationLong, Integer newNumItemsMoved, Date newShipmentsStartDate) {
        this.accountCompany = accountCompany;
        this.newMovementReason = newMovementReason;
        this.newSpecies = newSpecies;
        this.newOriginAddress = newOriginAddress;
        this.newOriginCity = newOriginCity;
        this.newOriginName = newOriginName;
        this.newOriginPostalCode = newOriginPostalCode;
        this.newOriginPremId = newOriginPremId;
        this.newOriginState = newOriginState;
        this.newDestinationAddress = newDestinationAddress;
        this.newDestinationCity = newDestinationCity;
        this.newDestinationName = newDestinationName;
        this.newDestinationPostalCode = newDestinationPostalCode;
        this.newDestinationPremId = newDestinationPremId;
        this.newDestinationState = newDestinationState;
        this.originLat = originLat;
        this.originLon = originLon;
        this.destinationLat = destinationLat;
        this.destinationLong = destinationLong;
        this.newNumItemsMoved = newNumItemsMoved;
        this.newShipmentsStartDate = newShipmentsStartDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountCompany() {
        return accountCompany;
    }

    public void setAccountCompany(String accountCompany) {
        this.accountCompany = accountCompany;
    }

    public String getNewMovementReason() {
        return newMovementReason;
    }

    public void setNewMovementReason(String newMovementReason) {
        this.newMovementReason = newMovementReason;
    }

    public String getNewSpecies() {
        return newSpecies;
    }

    public void setNewSpecies(String newSpecies) {
        this.newSpecies = newSpecies;
    }

    public String getNewOriginAddress() {
        return newOriginAddress;
    }

    public void setNewOriginAddress(String newOriginAddress) {
        this.newOriginAddress = newOriginAddress;
    }

    public String getNewOriginCity() {
        return newOriginCity;
    }

    public void setNewOriginCity(String newOriginCity) {
        this.newOriginCity = newOriginCity;
    }

    public String getNewOriginName() {
        return newOriginName;
    }

    public void setNewOriginName(String newOriginName) {
        this.newOriginName = newOriginName;
    }

    public String getNewOriginPostalCode() {
        return newOriginPostalCode;
    }

    public void setNewOriginPostalCode(String newOriginPostalCode) {
        this.newOriginPostalCode = newOriginPostalCode;
    }

    public String getNewOriginPremId() {
        return newOriginPremId;
    }

    public void setNewOriginPremId(String newOriginPremId) {
        this.newOriginPremId = newOriginPremId;
    }

    public String getNewOriginState() {
        return newOriginState;
    }

    public void setNewOriginState(String newOriginState) {
        this.newOriginState = newOriginState;
    }

    public String getNewDestinationAddress() {
        return newDestinationAddress;
    }

    public void setNewDestinationAddress(String newDestinationAddress) {
        this.newDestinationAddress = newDestinationAddress;
    }

    public String getNewDestinationCity() {
        return newDestinationCity;
    }

    public void setNewDestinationCity(String newDestinationCity) {
        this.newDestinationCity = newDestinationCity;
    }

    public String getNewDestinationName() {
        return newDestinationName;
    }

    public void setNewDestinationName(String newDestinationName) {
        this.newDestinationName = newDestinationName;
    }

    public String getNewDestinationPostalCode() {
        return newDestinationPostalCode;
    }

    public void setNewDestinationPostalCode(String newDestinationPostalCode) {
        this.newDestinationPostalCode = newDestinationPostalCode;
    }

    public String getNewDestinationPremId() {
        return newDestinationPremId;
    }

    public void setNewDestinationPremId(String newDestinationPremId) {
        this.newDestinationPremId = newDestinationPremId;
    }

    public String getNewDestinationState() {
        return newDestinationState;
    }

    public void setNewDestinationState(String newDestinationState) {
        this.newDestinationState = newDestinationState;
    }

    public Double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(Double originLat) {
        this.originLat = originLat;
    }

    public Double getOriginLon() {
        return originLon;
    }

    public void setOriginLon(Double originLon) {
        this.originLon = originLon;
    }

    public Double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public Double getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(Double destinationLong) {
        this.destinationLong = destinationLong;
    }

    public Integer getNewNumItemsMoved() {
        return newNumItemsMoved;
    }

    public void setNewNumItemsMoved(Integer newNumItemsMoved) {
        this.newNumItemsMoved = newNumItemsMoved;
    }

    public Date getNewShipmentsStartDate() {
        return newShipmentsStartDate;
    }

    public void setNewShipmentsStartDate(Date newShipmentsStartDate) {
        this.newShipmentsStartDate = newShipmentsStartDate;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Movement{" +
                "id=" + id +
                ", accountCompany='" + accountCompany + '\'' +
                ", newMovementReason='" + newMovementReason + '\'' +
                ", newSpecies='" + newSpecies + '\'' +
                ", newOriginAddress='" + newOriginAddress + '\'' +
                ", newOriginCity='" + newOriginCity + '\'' +
                ", newOriginName='" + newOriginName + '\'' +
                ", newOriginPostalCode='" + newOriginPostalCode + '\'' +
                ", newOriginPremId='" + newOriginPremId + '\'' +
                ", newOriginState='" + newOriginState + '\'' +
                ", newDestinationAddress='" + newDestinationAddress + '\'' +
                ", newDestinationCity='" + newDestinationCity + '\'' +
                ", newDestinationName='" + newDestinationName + '\'' +
                ", newDestinationPostalCode='" + newDestinationPostalCode + '\'' +
                ", newDestinationPremId='" + newDestinationPremId + '\'' +
                ", newDestinationState='" + newDestinationState + '\'' +
                ", originLat=" + originLat +
                ", originLon=" + originLon +
                ", destinationLat=" + destinationLat +
                ", destinationLong=" + destinationLong +
                ", newNumItemsMoved=" + newNumItemsMoved +
                ", newShipmentsStartDate=" + newShipmentsStartDate +
                '}';
    }
}