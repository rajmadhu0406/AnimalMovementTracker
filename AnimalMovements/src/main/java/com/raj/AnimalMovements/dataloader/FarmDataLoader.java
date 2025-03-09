package com.raj.AnimalMovements.dataloader;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.service.FarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FarmDataLoader implements CommandLineRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(FarmDataLoader.class);
    private final FarmService farmService;

    public FarmDataLoader(FarmService farmService) {
        this.farmService = farmService;
    }

    @Override
    public void run(String... args) {
        logger.info("Loading farm data...");
        String csvFilePath = "src/main/resources/data/population.csv";
        loadFarmDataFromCsv(csvFilePath);
        logger.info("Farm data loaded successfully");
    }

    public void loadFarmDataFromCsv(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<FarmCsvRecord> csvToBean = new CsvToBeanBuilder<FarmCsvRecord>(reader)
                    .withType(FarmCsvRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            csvToBean.parse().forEach(record -> {
                logger.debug("Processing record: {}", record);

                // Check if farm already exists
                Farm existingFarm = farmService.getFarmByPremiseId(record.getPremiseId());
                if (existingFarm == null) {
                    Farm farm = new Farm();
                    farm.setPremiseId(record.getPremiseId());
                    farm.setTotalAnimal(record.getTotalAnimal());
                    farm.setLatitude(record.getLatitude());
                    farm.setLongitude(record.getLongitude());
                    farm.setAddress(record.getAddress());
                    farm.setState(record.getState());
                    farm.setCity(record.getCity());
                    farm.setName(record.getName());
                    farm.setPostalCode(record.getPostalCode());

                    farmService.saveFarm(farm);
                    logger.info("Saved farm with premiseId: {}", record.getPremiseId());
                } else {
                    logger.info("Farm with premiseId: {} already exists, skipping...", record.getPremiseId());
                }
            });

            logger.info("Farm data loading completed!");

        } catch (IOException e) {
            logger.error("Error reading the CSV file: {}", e.getMessage());
        }
    }

    // Inner class representing a row in the CSV file
    public static class FarmCsvRecord {
        @CsvBindByName(column = "premise_id")
        private String premiseId;

        @CsvBindByName(column = "total_animal")
        private int totalAnimal;

        @CsvBindByName(column = "latitude")
        private Double latitude;

        @CsvBindByName(column = "longitude")
        private Double longitude;

        @CsvBindByName(column = "address")
        private String address;

        @CsvBindByName(column = "state")
        private String state;

        @CsvBindByName(column = "city")
        private String city;

        @CsvBindByName(column = "name")
        private String name;

        @CsvBindByName(column = "postal_code")
        private String postalCode;

        // Getters and Setters
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
    }

    @Override
    public int getOrder() {
        return 3; // Lower value means higher priority
    }
}
