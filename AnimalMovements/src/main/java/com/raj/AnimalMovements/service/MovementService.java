package com.raj.AnimalMovements.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.model.Movement;
import com.raj.AnimalMovements.repository.MovementRepository;

@Service
public class MovementService {

    private static final Logger logger = LoggerFactory.getLogger(MovementService.class);

    private final MovementRepository movementRepository;
    private final FarmService farmService;

    public MovementService(MovementRepository movementRepository, FarmService farmService) {
        this.movementRepository = movementRepository;
        this.farmService = farmService;
    }

    // Save a movement
    public Movement saveMovement(Movement movement) {

        // get farm by origin premiseId
        Farm originFarm = farmService.getFarmByPremiseId(movement.getNewOriginPremId());

        // get farm by destination premiseId
        Farm destinationFarm = farmService.getFarmByPremiseId(movement.getNewDestinationPremId());

        // if both farms are not null, then save the movement
        if (originFarm != null && destinationFarm != null) {

            Integer numItemsMoved = movement.getNewNumItemsMoved();

            if(numItemsMoved <= 0){
                throw new RuntimeException("Number of items moved must be greater than 0");
            }

            if(originFarm.getTotalAnimal() - numItemsMoved < 0){
                throw new RuntimeException("Origin farm does not have enough animals to move");
            }

            originFarm.setTotalAnimal(originFarm.getTotalAnimal() - numItemsMoved);
            destinationFarm.setTotalAnimal(destinationFarm.getTotalAnimal() + numItemsMoved);

            // update the farms
            farmService.saveFarm(originFarm);
            farmService.saveFarm(destinationFarm);

            return movementRepository.save(movement);
        } else {
            logger.error("Farm not found for premiseId: {}", movement.getNewOriginPremId());
            logger.error("OR Farm not found for premiseId: {}", movement.getNewDestinationPremId());
            throw new RuntimeException("Farm not found for premiseId: " + movement.getNewOriginPremId() + " or "
                    + movement.getNewDestinationPremId());
        }

    }

    // Get all movements
    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    // Get a movement by ID
    public Optional<Movement> getMovementById(Long id) {
        return movementRepository.findById(id);
    }

    // Get a movement by origin premiseId
    public Movement getMovementByOriginPremId(String newOriginPremId) {
        return movementRepository.findByNewOriginPremId(newOriginPremId);
    }

    // Get a movement by destination premiseId
    public Movement getMovementByDestinationPremId(String newDestinationPremId) {
        return movementRepository.findByNewDestinationPremId(newDestinationPremId);
    }

    // Update a movement
    // public Movement updateMovement(Long id, Movement movementDetails) {
    // Movement movement = movementRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Movement not found with id: " +
    // id));
    // movement.setAccountCompany(movementDetails.getAccountCompany());
    // movement.setNewMovementReason(movementDetails.getNewMovementReason());
    // movement.setNewSpecies(movementDetails.getNewSpecies());
    // movement.setNewOriginAddress(movementDetails.getNewOriginAddress());
    // movement.setNewOriginCity(movementDetails.getNewOriginCity());
    // movement.setNewOriginName(movementDetails.getNewOriginName());
    // movement.setNewOriginPostalCode(movementDetails.getNewOriginPostalCode());
    // movement.setNewOriginPremId(movementDetails.getNewOriginPremId());
    // movement.setNewOriginState(movementDetails.getNewOriginState());
    // movement.setNewDestinationAddress(movementDetails.getNewDestinationAddress());
    // movement.setNewDestinationCity(movementDetails.getNewDestinationCity());
    // movement.setNewDestinationName(movementDetails.getNewDestinationName());
    // movement.setNewDestinationPostalCode(movementDetails.getNewDestinationPostalCode());
    // movement.setNewDestinationPremId(movementDetails.getNewDestinationPremId());
    // movement.setNewDestinationState(movementDetails.getNewDestinationState());
    // movement.setOriginLat(movementDetails.getOriginLat());
    // movement.setOriginLon(movementDetails.getOriginLon());
    // movement.setDestinationLat(movementDetails.getDestinationLat());
    // movement.setDestinationLong(movementDetails.getDestinationLong());
    // movement.setNewNumItemsMoved(movementDetails.getNewNumItemsMoved());
    // movement.setNewShipmentsStartDate(movementDetails.getNewShipmentsStartDate());
    // return movementRepository.save(movement);
    // }

    // Delete a movement
    public void deleteMovement(Long id) {

        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movement not found with id: " + id));

        // get farm by origin premiseId
        Farm originFarm = farmService.getFarmByPremiseId(movement.getNewOriginPremId());

        // get farm by destination premiseId
        Farm destinationFarm = farmService.getFarmByPremiseId(movement.getNewDestinationPremId());

        if (originFarm != null && destinationFarm != null) {

            int numItemsMoved = movement.getNewNumItemsMoved();

            if(destinationFarm.getTotalAnimal() - numItemsMoved < 0){
                throw new RuntimeException("Destination farm does not have enough animals to move");
            }

            // update the farms
            originFarm.setTotalAnimal(originFarm.getTotalAnimal() + numItemsMoved);
            destinationFarm.setTotalAnimal(destinationFarm.getTotalAnimal() - numItemsMoved);

            // save the farms
            farmService.saveFarm(originFarm);
            farmService.saveFarm(destinationFarm);

            movementRepository.deleteById(id);
        } else {
            logger.error("Farm not found for premiseId: {}", movement.getNewOriginPremId());
            logger.error("OR Farm not found for premiseId: {}", movement.getNewDestinationPremId());
            throw new RuntimeException("Farm not found for premiseId: " + movement.getNewOriginPremId() + " or "
                    + movement.getNewDestinationPremId());
        }
    }

}
