package com.raj.AnimalMovements.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.model.Movement;
import com.raj.AnimalMovements.model.MovementDetailsDTO;
import com.raj.AnimalMovements.repository.FarmRepository;
import com.raj.AnimalMovements.repository.MovementRepository;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final FarmRepository farmRepository;

    public MovementService(MovementRepository movementRepository, FarmRepository farmRepository) {
        this.movementRepository = movementRepository;
        this.farmRepository = farmRepository;
    }

    // Create a new movement
    public Movement createMovement(Movement movement) {
        Farm originFarm = farmRepository.findByPremiseId(movement.getNewOriginFarm().getPremiseId());
        if (originFarm == null) {
            throw new RuntimeException("Origin farm not found: " + movement.getNewOriginFarm().getPremiseId());
        }

        Farm destinationFarm = farmRepository.findByPremiseId(movement.getNewDestinationFarm().getPremiseId());
        if (destinationFarm == null) {
            throw new RuntimeException("Destination farm not found: " + movement.getNewDestinationFarm().getPremiseId());
        }

        if (originFarm.getPremiseId().equals(destinationFarm.getPremiseId())) {
            throw new RuntimeException("Origin and destination farms cannot be the same");
        }

        originFarm.setTotalAnimal(originFarm.getTotalAnimal() - movement.getNewNumItemsMoved());
        destinationFarm.setTotalAnimal(destinationFarm.getTotalAnimal() + movement.getNewNumItemsMoved());
        
        farmRepository.save(originFarm);
        farmRepository.save(destinationFarm);

        movement.setNewOriginFarm(originFarm);
        movement.setNewDestinationFarm(destinationFarm);

        return movementRepository.save(movement);
    }

    //  Get all movements
    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }


    // Get movement by ID
    public Optional<Movement> getMovementById(Long id) {
        return movementRepository.findById(id);
    }

    // Update a movement
    // public Movement updateMovement(Long id, Movement movementDetails) {
    //     Movement movement = movementRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Movement not found with id: " + id));
    //     Farm originFarm = farmRepository.findByPremiseId(movementDetails.getNewOriginFarm().getPremiseId());
    //     if (originFarm == null) {
    //         throw new RuntimeException("Origin farm not found: " + movementDetails.getNewOriginFarm().getPremiseId());
    //     }

    //     Farm destinationFarm = farmRepository.findByPremiseId(movementDetails.getNewDestinationFarm().getPremiseId());
    //     if (destinationFarm == null) {
    //         throw new RuntimeException("Destination farm not found: " + movementDetails.getNewDestinationFarm().getPremiseId());
    //     }

    //     movement.setAccountCompany(movementDetails.getAccountCompany());
    //     movement.setNewMovementReason(movementDetails.getNewMovementReason());
    //     movement.setNewNumItemsMoved(movementDetails.getNewNumItemsMoved());
    //     movement.setNewShipmentsStartDate(movementDetails.getNewShipmentsStartDate());
    //     movement.setNewSpecies(movementDetails.getNewSpecies());
    //     movement.setNewOriginFarm(originFarm);
    //     movement.setNewDestinationFarm(destinationFarm);

    //     return movementRepository.save(movement);
    // }

    // Delete a movement
    public void deleteMovement(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movement not found with id: " + id));
        movementRepository.delete(movement);
    }

    // Get movements by origin farm
    public List<Movement> getMovementsByOriginFarm(String premiseId) {
        return movementRepository.findByNewOriginFarm_PremiseId(premiseId);
    }

    // Get movements by destination farm
    public List<Movement> getMovementsByDestinationFarm(String premiseId) {
        return movementRepository.findByNewDestinationFarm_PremiseId(premiseId);
    }

    // Get all movement details with farm info
    @Transactional(readOnly = true)
    public List<MovementDetailsDTO> getAllMovementDetails() {
        return movementRepository.findAll().stream().map(movement -> new MovementDetailsDTO(
                movement.getId(),
                movement.getNewOriginFarm().getCity(),
                movement.getNewOriginFarm().getState(),
                movement.getNewDestinationFarm().getName(),
                movement.getNewDestinationFarm().getPostalCode(),
                movement.getNewDestinationFarm().getAddress(),
                movement.getNewDestinationFarm().getPremiseId(),
                movement.getNewShipmentsStartDate(),
                movement.getNewOriginFarm().getAddress(),
                movement.getNewMovementReason(),
                movement.getNewDestinationFarm().getState(),
                movement.getNewSpecies(),
                movement.getNewOriginFarm().getPostalCode(),
                movement.getNewNumItemsMoved(),
                movement.getNewDestinationFarm().getCity(),
                movement.getAccountCompany(),
                movement.getNewOriginFarm().getName(),
                movement.getNewOriginFarm().getPremiseId(),
                movement.getNewOriginFarm().getLongitude(),
                movement.getNewDestinationFarm().getLatitude(),
                movement.getNewDestinationFarm().getLongitude(),
                movement.getNewOriginFarm().getLatitude()
        )).collect(Collectors.toList());
    }

    // Get movement details by ID
    public MovementDetailsDTO getMovementDetailsById(Long id) {
        return getAllMovementDetails().stream()
                .filter(movement -> movement.getMovementId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Movement details not found with id: " + id));
    }       
    
    // Get movement details by orinal farm premise id
    public List<MovementDetailsDTO> getMovementDetailsByOriginFarmPremiseId(String premiseId) {
        return getAllMovementDetails().stream()
                .filter(movement -> movement.getNewOriginPremId().equals(premiseId))
                .collect(Collectors.toList());
    }       

    // Get movement details by destination farm premise id
    public List<MovementDetailsDTO> getMovementDetailsByDestinationFarmPremiseId(String premiseId) {
        return getAllMovementDetails().stream()
                .filter(movement -> movement.getNewDestinationPremId().equals(premiseId))
                .collect(Collectors.toList());
    }

}

