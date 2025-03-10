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
public class UserDataLoader implements CommandLineRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(UserDataLoader.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDataLoader(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there's at least one user with the ADMIN role
        boolean adminExists = userRepository.existsByRole_RoleType(RoleType.ADMIN);
        boolean viewerExists = userRepository.existsByRole_RoleType(RoleType.VIEWER);
        boolean userExists = userRepository.existsByRole_RoleType(RoleType.USER);

        if (!adminExists) {
            createAdminUser();
        }
        else{
            logger.info("ADMIN user already exists, skipping creation");
        }

        if (!userExists) {
            createUserUser();
        }
        else{
            logger.info("USER user already exists, skipping creation");
        }

        if (!viewerExists) {
            createViewerUser();
        }
        else{
            logger.info("VIEWER user already exists, skipping creation");
        }
        
        
    }

    // Create the admin user
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

    //create the VIEWER user
    private void createViewerUser() {
        // 1) Ensure the VIEWER role exists
        Role viewerRole = roleRepository.findByRoleType(RoleType.VIEWER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleType(RoleType.VIEWER);
                    return roleRepository.save(newRole);
                });

        // 2) Create the viewer user
        User viewerUser = new User();
        viewerUser.setFirstName("Viewer");
        viewerUser.setLastName("User");
        viewerUser.setEmail("viewer@viewer.com");
        viewerUser.setUsername("viewer");
        viewerUser.setPassword("viewer123"); // Will be encoded in userService
        viewerUser.setRole(viewerRole);

        // 3) Save using your UserService to ensure the password is encoded
        userService.createUser(viewerUser);
    }

    //create the USER user
    private void createUserUser() {
        // 1) Ensure the USER role exists
        Role userRole = roleRepository.findByRoleType(RoleType.USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleType(RoleType.USER);
                    return roleRepository.save(newRole);
                });

        // 2) Create the user user
        User userUser = new User();
        userUser.setFirstName("User");  
        userUser.setLastName("User");
        userUser.setEmail("user@user.com");
        userUser.setUsername("user");
        userUser.setPassword("user123"); // Will be encoded in userService
        userUser.setRole(userRole);

        // 3) Save using your UserService to ensure the password is encoded
        userService.createUser(userUser);
    }


    @Override
    public int getOrder() {
        return 2; //lower value means higher priority
    }

}
