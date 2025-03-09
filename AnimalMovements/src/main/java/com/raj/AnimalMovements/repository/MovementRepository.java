package com.raj.AnimalMovements.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raj.AnimalMovements.model.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
        // Find movements by origin farm
        List<Movement> findByNewOriginFarm_PremiseId(String premiseId);

        // Find movements by destination farm
        List<Movement> findByNewDestinationFarm_PremiseId(String premiseId);
    
}
