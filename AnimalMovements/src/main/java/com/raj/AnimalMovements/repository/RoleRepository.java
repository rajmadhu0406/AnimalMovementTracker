package com.raj.AnimalMovements.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raj.AnimalMovements.model.Role;
import com.raj.AnimalMovements.model.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Find a role by its RoleType (e.g., ADMIN, USER)
    Optional<Role> findByRoleType(RoleType roleType);

    // Check if a role exists
    boolean existsByRoleType(RoleType roleType);
}
