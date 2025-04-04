package com.raj.AnimalMovements.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.repository.FarmRepository;

@Service
public class FarmService {

    private static final Logger logger = LoggerFactory.getLogger(FarmService.class);

    private final FarmRepository farmRepository;

    public FarmService(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    public Farm saveFarm(Farm farm) {
        return farmRepository.save(farm);
    }

    // Get all farms
    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }

    // Get a farm by ID
    public Optional<Farm> getFarmById(Long id) {
        return farmRepository.findById(id);
    }

    // Get a farm by premiseId
    public Farm getFarmByPremiseId(String premiseId) {
        return farmRepository.findByPremiseId(premiseId);
    }

    /**
     * The `updateFarm` function updates the details of a farm entity based on the
     * provided farmDetails object.
     */
    public Farm updateFarm(Long id, Farm farmDetails) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farm not found with id: " + id));

        // Update all fields
        farm.setPremiseId(farmDetails.getPremiseId());
        farm.setTotalAnimal(farmDetails.getTotalAnimal());
        farm.setLatitude(farmDetails.getLatitude());
        farm.setLongitude(farmDetails.getLongitude());
        farm.setAddress(farmDetails.getAddress());
        farm.setState(farmDetails.getState());
        farm.setCity(farmDetails.getCity());
        farm.setName(farmDetails.getName());
        farm.setPostalCode(farmDetails.getPostalCode());

        return farmRepository.save(farm);
    }

    // Delete a farm
    public void deleteFarm(Long id) {
        farmRepository.deleteById(id);
    }

}
