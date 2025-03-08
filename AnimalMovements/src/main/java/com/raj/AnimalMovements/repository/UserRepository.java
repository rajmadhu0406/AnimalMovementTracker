package com.raj.AnimalMovements.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raj.AnimalMovements.model.RoleType;
import com.raj.AnimalMovements.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Object>{
    
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByRole_RoleType(RoleType roleType);

}
