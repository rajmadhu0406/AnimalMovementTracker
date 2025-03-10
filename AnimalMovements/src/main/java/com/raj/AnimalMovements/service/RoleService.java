package com.raj.AnimalMovements.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.raj.AnimalMovements.model.Role;
import com.raj.AnimalMovements.model.RoleType;
import com.raj.AnimalMovements.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getRoleByType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role createRole(Role role) {
        // Check if the role already exists by its type
        if (roleRepository.existsByRoleType(role.getRoleType())) {
            // Log an error message if the role exists
            logger.error("Role already exists: {}", role.getRoleType());
            // Throw an exception to indicate the role already exists
            throw new IllegalArgumentException("Role already exists!");
        }
        // Save the new role to the repository
        return roleRepository.save(role);
    }

    public boolean roleExists(RoleType roleType) {
        return roleRepository.findByRoleType(roleType).isPresent();
    }
}
