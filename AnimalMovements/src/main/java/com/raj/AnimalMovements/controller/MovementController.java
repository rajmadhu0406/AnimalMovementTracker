package com.raj.AnimalMovements.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raj.AnimalMovements.model.Movement;
import com.raj.AnimalMovements.service.MovementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@RequestMapping("/api/movement")
@Tag(name = "Movement API", description = "APIs for managing Movements")
public class MovementController {

    MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all movements", description = "Accessible by ADMIN, VIEWER, and USER roles")
    public ResponseEntity<List<Movement>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get movement by ID", description = "Accessible by ADMIN, VIEWER, and USER roles")
    public ResponseEntity<Movement> getMovementById(@PathVariable Long id) {
        return movementService.getMovementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/premise/origin/{premiseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get movements by origin premise ID", description = "Accessible by ADMIN, VIEWER, and USER roles")
    public ResponseEntity<List<Movement>> getMovementsByOriginPremiseId(@PathVariable String premiseId) {
        List<Movement> movements = movementService.getMovementsByOriginFarm(premiseId);
        return movements.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(movements);
    }

    @GetMapping("/premise/destination/{premiseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get movements by destination premise ID", description = "Accessible by ADMIN, VIEWER, and USER roles")
    public ResponseEntity<List<Movement>> getMovementsByDestinationPremiseId(@PathVariable String premiseId) {
        List<Movement> movements = movementService.getMovementsByDestinationFarm(premiseId);
        return movements.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(movements);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new movement", description = "Accessible by ADMIN and USER roles")
    public ResponseEntity<?> createMovement(@RequestBody Movement movement) {
        try {
            Movement savedMovement = movementService.createMovement(movement);
            return ResponseEntity.ok(savedMovement);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    @Operation(summary = "Delete a movement", description = "Accessible by ADMIN")
    public ResponseEntity<String> deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
        return ResponseEntity.ok("Movement deleted successfully");
    }
    
}
