package com.raj.AnimalMovements.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raj.AnimalMovements.model.Role;
import com.raj.AnimalMovements.model.User;
import com.raj.AnimalMovements.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("Username already exists: {}", user.getUsername());
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new RuntimeException("Email already exists");
        }

        if (user.getRole().getRoleType() != null) {
            Role role = roleService.getRoleByType(user.getRole().getRoleType())
                    .orElseThrow(() -> new RuntimeException("Invalid role type: " + user.getRole().getRoleType()));
            user.setRole(role);

        } else if (user.getRole().getId() != null) {
            Role role = roleService.getRoleById(user.getRole().getId()).orElseThrow();
            user.setRole(role);
        } else {
            logger.error("User has no role: {}", user.getUsername());
            throw new RuntimeException("User has no role");
        }

        // encoding the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }

    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new RuntimeException("User not found with ID: " + id);
                });
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            // Update user details (if provided)
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
                if (userRepository.existsByEmail(updatedUser.getEmail())) {
                    logger.error("Email already exists: {}", updatedUser.getEmail());
                    throw new RuntimeException("Email already exists");
                }
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())) {
                if (userRepository.existsByUsername(updatedUser.getUsername())) {
                    logger.error("Username already exists: {}", updatedUser.getUsername());
                    throw new RuntimeException("Username already exists");
                }
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }

            logger.info("User updated successfully: {}", id);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found with ID: " + id);
        });
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            logger.error("Attempted to delete non-existent user with ID: {}", id);
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        logger.info("User deleted successfully: {}", id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // Method to load user details by username for authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user details from the database
        User user = findByUsername(username);

        // Create a list of authorities (roles) for the user
        List<GrantedAuthority> authorities = new ArrayList<>();

        // If the user has a role, add it to the authorities list
        if (user.getRole() != null) {
            // Note: Spring Security expects roles to be prefixed with "ROLE_"
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleType().name()));
        } else {
            logger.error("User has no role: {}", user.getUsername());
            throw new RuntimeException("User has no role");
        }

        // Return a Spring Security User object containing the username, password, and
        // granted authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

}
