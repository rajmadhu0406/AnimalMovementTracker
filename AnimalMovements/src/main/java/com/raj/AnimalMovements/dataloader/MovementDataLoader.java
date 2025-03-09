package com.raj.AnimalMovements.dataloader;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.model.Movement;
import com.raj.AnimalMovements.service.FarmService;
import com.raj.AnimalMovements.service.MovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MovementDataLoader implements CommandLineRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(MovementDataLoader.class);

    private final MovementService movementService;
    private final FarmService farmService;

    public MovementDataLoader(MovementService movementService, FarmService farmService) {
        this.movementService = movementService;
        this.farmService = farmService;
    }

    @Override
    public void run(String... args) {
        logger.info("Loading movement data...");
        String csvFilePath = "src/main/resources/data/Movement.csv";
        loadMovementDataFromCsv(csvFilePath);
        logger.info("Movement data loaded successfully");
    }

    public void loadMovementDataFromCsv(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<MovementCsvRecord> csvToBean = new CsvToBeanBuilder<MovementCsvRecord>(reader)
                    .withType(MovementCsvRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            csvToBean.parse().forEach(record -> {
                logger.debug("Processing record: {}", record.toString());

                // Validate record
                if (!isValidMovementCsvRecord(record)) {
                    logger.warn("Skipping invalid record: {}", record.getNewOriginPremId());
                    return;
                }

                // Fetch origin and destination farms
                Farm originFarm = farmService.getFarmByPremiseId(record.getNewOriginPremId());
                Farm destinationFarm = farmService.getFarmByPremiseId(record.getNewDestinationPremId());

                if (originFarm == null || destinationFarm == null) {
                    logger.warn("Skipping movement - Origin or destination farm not found: Origin {}, Destination {}",
                            record.getNewOriginPremId(), record.getNewDestinationPremId());
                    return;
                }

                // Check if movement already exists
                if (movementService.getMovementsByOriginFarm(record.getNewOriginPremId()).isEmpty()) {
                    Movement movement = new Movement();
                    movement.setAccountCompany(record.getAccountCompany());
                    movement.setNewMovementReason(record.getNewMovementReason());
                    movement.setNewSpecies(record.getNewSpecies());
                    movement.setNewNumItemsMoved(record.getNewNumItemsMoved());
                    movement.setNewShipmentsStartDate(parseDate(record.getNewShipmentsStartDate()));
                    movement.setNewOriginFarm(originFarm);
                    movement.setNewDestinationFarm(destinationFarm);

                    // Save movement
                    movementService.createMovement(movement);
                    logger.info("Saved movement with origin premise ID: {}", record.getNewOriginPremId());
                } else {
                    logger.info("Movement with origin premise ID: {} already exists, skipping...", record.getNewOriginPremId());
                }
            });

            logger.info("Movement data loading completed!");

        } catch (IOException e) {
            logger.error("Error reading the CSV file: {}", e.getMessage());
        }
    }

    // Helper method to validate CSV records
    private boolean isValidMovementCsvRecord(MovementCsvRecord record) {
        return record.getAccountCompany() != null && !record.getAccountCompany().isEmpty() &&
               record.getNewMovementReason() != null && !record.getNewMovementReason().isEmpty() &&
               record.getNewSpecies() != null && !record.getNewSpecies().isEmpty() &&
               record.getNewOriginPremId() != null && !record.getNewOriginPremId().isEmpty() &&
               record.getNewDestinationPremId() != null && !record.getNewDestinationPremId().isEmpty() &&
               record.getNewNumItemsMoved() != null &&
               record.getNewShipmentsStartDate() != null && !record.getNewShipmentsStartDate().isEmpty();
    }

    // Helper method to parse date strings
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("Error parsing date: {}", dateStr, e);
            return null;
        }
    }

   
    // Inner class to represent a single row in the CSV file
    public static class MovementCsvRecord {
        @CsvBindByName(column = "account_company")
        private String accountCompany;

        @CsvBindByName(column = "new_movementreason")
        private String newMovementReason;

        @CsvBindByName(column = "new_species")
        private String newSpecies;

        @CsvBindByName(column = "new_originfarm")
        private String newOriginPremId;

        @CsvBindByName(column = "new_destinationfarm")
        private String newDestinationPremId;

        @CsvBindByName(column = "new_numitemsmoved")
        private Integer newNumItemsMoved;

        @CsvBindByName(column = "new_shipmentsstartdate")
        private String newShipmentsStartDate;

        // Getters and Setters
        public String getAccountCompany() { return accountCompany; }
        public void setAccountCompany(String accountCompany) { this.accountCompany = accountCompany; }

        public String getNewMovementReason() { return newMovementReason; }
        public void setNewMovementReason(String newMovementReason) { this.newMovementReason = newMovementReason; }

        public String getNewSpecies() { return newSpecies; }
        public void setNewSpecies(String newSpecies) { this.newSpecies = newSpecies; }

        public String getNewOriginPremId() { return newOriginPremId; }
        public void setNewOriginPremId(String newOriginPremId) { this.newOriginPremId = newOriginPremId; }

        public String getNewDestinationPremId() { return newDestinationPremId; }
        public void setNewDestinationPremId(String newDestinationPremId) { this.newDestinationPremId = newDestinationPremId; }

        public Integer getNewNumItemsMoved() { return newNumItemsMoved; }
        public void setNewNumItemsMoved(Integer newNumItemsMoved) { this.newNumItemsMoved = newNumItemsMoved; }

        public String getNewShipmentsStartDate() { return newShipmentsStartDate; }
        public void setNewShipmentsStartDate(String newShipmentsStartDate) { this.newShipmentsStartDate = newShipmentsStartDate; }
    }

    @Override
    public int getOrder() {
        return 4;
    }

}
