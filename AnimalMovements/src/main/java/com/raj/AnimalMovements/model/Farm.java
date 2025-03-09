package com.raj.AnimalMovements.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "farms", uniqueConstraints = @UniqueConstraint(columnNames = "premise_id"))
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

    @NotNull(message = "Latitude is required")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotBlank(message = "Address is required")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "State is required")
    @Column(name = "state", nullable = false)
    private String state;

    @NotBlank(message = "City is required")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Postal Code is required")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    // Default constructor
    public Farm() {
    }

    // Parameterized constructor
    public Farm(String premiseId, int totalAnimal, Double latitude, Double longitude, String address, String state, String city, String name, String postalCode) {
        this.premiseId = premiseId;
        this.totalAnimal = totalAnimal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.state = state;
        this.city = city;
        this.name = name;
        this.postalCode = postalCode;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPremiseId() { return premiseId; }
    public void setPremiseId(String premiseId) { this.premiseId = premiseId; }

    public int getTotalAnimal() { return totalAnimal; }
    public void setTotalAnimal(int totalAnimal) { this.totalAnimal = totalAnimal; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", premiseId='" + premiseId + '\'' +
                ", totalAnimal=" + totalAnimal +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
