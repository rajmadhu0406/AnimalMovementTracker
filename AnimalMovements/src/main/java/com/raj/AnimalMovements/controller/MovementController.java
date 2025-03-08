package com.raj.AnimalMovements.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raj.AnimalMovements.model.Movement;
import com.raj.AnimalMovements.service.MovementService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/movement")
@Tag(name = "Movement API", description = "APIs for managing Movements")
public class MovementController {

    MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public List<Movement> getAllMovements() {
        return movementService.getAllMovements();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Movement getMovementById(@PathVariable Long id) {
        return movementService.getMovementById(id).orElse(null) ;
    }

    @GetMapping("/premise/origin/{premiseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Movement getMovementByOriginPremiseId(@PathVariable String premiseId) {
        return movementService.getMovementByOriginPremId(premiseId);
    }

    @GetMapping("/premise/destination/{premiseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Movement getMovementByDestinationPremiseId(@PathVariable String premiseId) {
        return movementService.getMovementByDestinationPremId(premiseId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Movement createMovement(@RequestBody Movement movement) {
        return movementService.saveMovement(movement);
    }

    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // @SecurityRequirement(name = "bearerAuth")
    // public Movement updateMovement(@PathVariable Long id, @RequestBody Movement movement) {
    //     return movementService.updateMovement(id, movement);
    // }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
    }
    
}
