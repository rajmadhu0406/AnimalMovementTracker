package com.raj.AnimalMovements.dataloader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.raj.AnimalMovements.model.Movement;
import com.raj.AnimalMovements.service.MovementService;

@Component
public class MovementDataLoader implements CommandLineRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(MovementDataLoader.class);

    private final MovementService movementService;

    public MovementDataLoader(MovementService movementService) {
        this.movementService = movementService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading movement data...");
        String csvFilePath = "src/main/resources/data/Movement.csv";
        loadMovementDataFromCsv(csvFilePath);
        logger.info("Movement data loaded successfully");
    }
    public void loadMovementDataFromCsv(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            // Parse the CSV file
            CsvToBean<MovementCsvRecord> csvToBean = new CsvToBeanBuilder<MovementCsvRecord>(reader)
                    .withType(MovementCsvRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Convert CSV records to MovementCsvRecord objects
            csvToBean.parse().forEach(record -> {
                logger.debug("Processing record: {}", record);

                // Validate the record
                if (!isValidMovementCsvRecord(record)) {
                    logger.warn("Skipping invalid record: {}", record.getNewOriginPremId());
                    return;
                }

                // Check if a movement with the same origin premise ID already exists
                if (movementService.getMovementByOriginPremId(record.getNewOriginPremId()) == null) {
                    // If it doesn't exist, create and save the movement
                    Movement movement = new Movement();
                    movement.setAccountCompany(record.getAccountCompany());
                    movement.setNewMovementReason(record.getNewMovementReason());
                    movement.setNewSpecies(record.getNewSpecies());
                    movement.setNewOriginAddress(record.getNewOriginAddress());
                    movement.setNewOriginCity(record.getNewOriginCity());
                    movement.setNewOriginName(record.getNewOriginName());
                    movement.setNewOriginPostalCode(record.getNewOriginPostalCode());
                    movement.setNewOriginPremId(record.getNewOriginPremId());
                    movement.setNewOriginState(record.getNewOriginState());
                    movement.setNewDestinationAddress(record.getNewDestinationAddress());
                    movement.setNewDestinationCity(record.getNewDestinationCity());
                    movement.setNewDestinationName(record.getNewDestinationName());
                    movement.setNewDestinationPostalCode(record.getNewDestinationPostalCode());
                    movement.setNewDestinationPremId(record.getNewDestinationPremId());
                    movement.setNewDestinationState(record.getNewDestinationState());
                    movement.setOriginLat(record.getOriginLat());
                    movement.setOriginLon(record.getOriginLon());
                    movement.setDestinationLat(record.getDestinationLat());
                    movement.setDestinationLong(record.getDestinationLong());
                    movement.setNewNumItemsMoved(record.getNewNumItemsMoved());

                    // Parse the date and handle null values
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
                    Date shipmentDate = null;
                    try {
                        shipmentDate = sdf.parse(record.getNewShipmentsStartDate());
                    } catch (ParseException e) {
                        logger.error("Error parsing date: {}", record.getNewShipmentsStartDate(), e);
                    }
                    if (shipmentDate != null) {
                        movement.setNewShipmentsStartDate(shipmentDate);
                    } else {
                        logger.warn("Skipping movement with invalid or missing date: {}", record.getNewOriginPremId());
                        return; // Skip saving this movement
                    }

                    // Save the movement
                    movementService.saveMovement(movement);
                    logger.info("Saved movement with origin premise ID: {}", record.getNewOriginPremId());
                } else {
                    // If it exists, skip saving
                    logger.info("Movement with origin premise ID: {} already exists, skipping...",
                            record.getNewOriginPremId());
                }
            });

            logger.info("Movement data loaded successfully!");

        } catch (IOException e) {
            logger.error("Error reading the CSV file: {}", e.getMessage());
        }
    }

    // Helper method to validate CSV records
    private boolean isValidMovementCsvRecord(MovementCsvRecord record) {
        return record.getAccountCompany() != null && !record.getAccountCompany().isEmpty() &&
               record.getNewMovementReason() != null && !record.getNewMovementReason().isEmpty() &&
               record.getNewSpecies() != null && !record.getNewSpecies().isEmpty() &&
               record.getNewOriginAddress() != null && !record.getNewOriginAddress().isEmpty() &&
               record.getNewOriginCity() != null && !record.getNewOriginCity().isEmpty() &&
               record.getNewOriginName() != null && !record.getNewOriginName().isEmpty() &&
               record.getNewOriginPostalCode() != null && !record.getNewOriginPostalCode().isEmpty() &&
               record.getNewOriginPremId() != null && !record.getNewOriginPremId().isEmpty() &&
               record.getNewOriginState() != null && !record.getNewOriginState().isEmpty() &&
               record.getNewDestinationAddress() != null && !record.getNewDestinationAddress().isEmpty() &&
               record.getNewDestinationCity() != null && !record.getNewDestinationCity().isEmpty() &&
               record.getNewDestinationName() != null && !record.getNewDestinationName().isEmpty() &&
               record.getNewDestinationPostalCode() != null && !record.getNewDestinationPostalCode().isEmpty() &&
               record.getNewDestinationPremId() != null && !record.getNewDestinationPremId().isEmpty() &&
               record.getNewDestinationState() != null && !record.getNewDestinationState().isEmpty() &&
               record.getOriginLat() != null && // Check for null
               record.getOriginLon() != null && // Check for null
               record.getDestinationLat() != null && // Check for null
               record.getDestinationLong() != null && // Check for null
               record.getNewNumItemsMoved() != null && // Check for null
               record.getNewShipmentsStartDate() != null && !record.getNewShipmentsStartDate().isEmpty();
    }
    
    // Helper method to parse date strings
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("Error parsing date: {}", dateStr, e);
            return null;
        }
    }


    // Inner class to represent a single row in the CSV file
    public static class MovementCsvRecord {
        @CsvBindByName(column = "account/company")
        private String accountCompany;

        @CsvBindByName(column = "new_movementreason")
        private String newMovementReason;

        @CsvBindByName(column = "new_species")
        private String newSpecies;

        @CsvBindByName(column = "new_originaddress")
        private String newOriginAddress;

        @CsvBindByName(column = "new_origincity")
        private String newOriginCity;

        @CsvBindByName(column = "new_originname")
        private String newOriginName;

        @CsvBindByName(column = "new_originpostalcode")
        private String newOriginPostalCode;

        @CsvBindByName(column = "new_originpremid")
        private String newOriginPremId;

        @CsvBindByName(column = "new_originstate")
        private String newOriginState;

        @CsvBindByName(column = "new_destinationaddress")
        private String newDestinationAddress;

        @CsvBindByName(column = "new_destinationcity")
        private String newDestinationCity;

        @CsvBindByName(column = "new_destinationname")
        private String newDestinationName;

        @CsvBindByName(column = "new_destinationpostalcode")
        private String newDestinationPostalCode;

        @CsvBindByName(column = "new_destinationpremid")
        private String newDestinationPremId;

        @CsvBindByName(column = "new_destinationstate")
        private String newDestinationState;

        @CsvBindByName(column = "origin_Lat")
        private Double originLat;

        @CsvBindByName(column = "origin_Lon")
        private Double originLon;

        @CsvBindByName(column = "destination_Lat")
        private Double destinationLat;

        @CsvBindByName(column = "destination_Long")
        private Double destinationLong;

        @CsvBindByName(column = "new_numitemsmoved")
        private Integer newNumItemsMoved;

        @CsvBindByName(column = "new_shipmentsstartdate")
        private String new_shipmentsstartdate;

        // Getters and setters
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

        public String getNewShipmentsStartDate() {
            return new_shipmentsstartdate;
        }

        public void setNewShipmentsStartDate(String newShipmentsStartDate) {
            this.new_shipmentsstartdate = newShipmentsStartDate;
        }
    }


    @Override
    public int getOrder() {
        return 4; // Lower value means higher priority
    }

}
