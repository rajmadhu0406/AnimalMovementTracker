package com.raj.AnimalMovements.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.raj.AnimalMovements.model.Role;
import com.raj.AnimalMovements.model.RoleType;
import com.raj.AnimalMovements.service.RoleService;


// Loads roles into the database
@Component
public class RoleDataLoader implements CommandLineRunner, Ordered{

    private static final Logger logger = LoggerFactory.getLogger(RoleDataLoader.class);

    private final RoleService roleService;


    public RoleDataLoader(RoleService roleService) {
        this.roleService = roleService;
    }

    // Runs when the application starts
    @Override
    public void run(String... args) throws Exception {
        for(RoleType roleType : RoleType.values()) {
            
            if (!roleService.roleExists(roleType)) {
                Role newRole = new Role(roleType);
                try {
                    roleService.createRole(newRole);
                } catch (Exception e) {
                    logger.error("Error creating role: {}", e.getMessage());
                }
            } else {
                logger.info("Role {} already exists, skipping creation", roleType);
            }
        }
    }

    @Override
    public int getOrder() {
        return 1; //lower value means higher priority
    }
}