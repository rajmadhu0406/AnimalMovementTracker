package com.raj.AnimalMovements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raj.AnimalMovements.model.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    Movement findByNewOriginPremId(String newOriginPremId);
    Movement findByNewDestinationPremId(String newDestinationPremId);
}
