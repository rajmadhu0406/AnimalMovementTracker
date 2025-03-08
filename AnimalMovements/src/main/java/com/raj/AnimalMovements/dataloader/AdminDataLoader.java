package com.raj.AnimalMovements.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.raj.AnimalMovements.model.Role;
import com.raj.AnimalMovements.model.RoleType;
import com.raj.AnimalMovements.model.User;
import com.raj.AnimalMovements.repository.RoleRepository;
import com.raj.AnimalMovements.repository.UserRepository;
import com.raj.AnimalMovements.service.UserService;

@Component
public class AdminDataLoader implements CommandLineRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AdminDataLoader.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminDataLoader(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there's at least one user with the ADMIN role
        boolean adminExists = userRepository.existsByRole_RoleType(RoleType.ADMIN);

        if (!adminExists) {
            createAdminUser();
        }
        else{
            logger.info("Admin user already exists, skipping creation");
        }
    }

    private void createAdminUser() {
        // 1) Ensure the ADMIN role exists
        Role adminRole = roleRepository.findByRoleType(RoleType.ADMIN)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleType(RoleType.ADMIN);
                    return roleRepository.save(newRole);
                });

        // 2) Create the admin user
        User adminUser = new User();
        adminUser.setFirstName("Super");
        adminUser.setLastName("Admin");
        adminUser.setEmail("admin@admin.com");
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123"); // Will be encoded in userService
        adminUser.setRole(adminRole);

        // 3) Save using your UserService to ensure the password is encoded
        userService.createUser(adminUser);
    }

    @Override
    public int getOrder() {
        return 2; //lower value means higher priority
    }

}
