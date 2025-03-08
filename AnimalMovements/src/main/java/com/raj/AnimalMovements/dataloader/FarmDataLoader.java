package com.raj.AnimalMovements.dataloader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.service.FarmService;

@Component
public class FarmDataLoader implements CommandLineRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(FarmDataLoader.class);

    private final FarmService farmService;

    public FarmDataLoader(FarmService farmService) {
        this.farmService = farmService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading farm data...");
        String csvFilePath = "src/main/resources/data/population.csv";
        loadFarmDataFromCsv(csvFilePath);
        logger.info("Farm data loaded successfully");
    }   
    
    public void loadFarmDataFromCsv(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            // Parse the CSV file
            CsvToBean<FarmCsvRecord> csvToBean = new CsvToBeanBuilder<FarmCsvRecord>(reader)
                    .withType(FarmCsvRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Convert CSV records to FarmCsvRecord objects
            csvToBean.parse().forEach(record -> {
                // Check if a farm with the same premiseId already exists
                if (farmService.getFarmByPremiseId(record.getPremiseId()) == null) {
                    // If it doesn't exist, create and save the farm
                    Farm farm = new Farm();
                    farm.setPremiseId(record.getPremiseId());
                    farm.setTotalAnimal(record.getTotalAnimal());
                    farmService.saveFarm(farm);
                    logger.info("Saved farm with premiseId: {}", record.getPremiseId());
                } else {
                    // If it exists, skip saving
                    logger.info("Farm with premiseId: {} already exists, skipping...", record.getPremiseId());
                }
            });

            logger.info("Farm data loaded successfully!");

        } catch (IOException e) {
            logger.error("Error reading the CSV file: {}", e.getMessage());
        }
    }


    // Inner class to represent a single row in the CSV file
    public static class FarmCsvRecord {
        private String premiseId;
        private int total_animal;

        // Getters and setters
        public String getPremiseId() {
            return premiseId;
        }

        public void setPremiseId(String premiseId) {
            this.premiseId = premiseId;
        }

        public int getTotalAnimal() {
            return total_animal;
        }

        public void setTotalAnimal(int total_animal) {
            this.total_animal = total_animal;
        }
    }


    @Override
    public int getOrder() {
        return 3; //lower value means higher priority
    }

    

}
