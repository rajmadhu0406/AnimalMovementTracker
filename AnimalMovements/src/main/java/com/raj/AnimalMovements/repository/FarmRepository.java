package com.raj.AnimalMovements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raj.AnimalMovements.model.Farm;

@Repository
public interface  FarmRepository extends JpaRepository<Farm, Long>{
    Farm findByPremiseId(String premiseId);
}
