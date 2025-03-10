package com.raj.AnimalMovements.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raj.AnimalMovements.model.Farm;
import com.raj.AnimalMovements.service.FarmService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/farm")
@Tag(name = "Farm API", description = "APIs for managing farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    // Create a new farm
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new farm", description = "Accessible by ADMIN")
    public ResponseEntity<Farm> createFarm(@Valid @RequestBody Farm farm) {
        Farm savedFarm = farmService.saveFarm(farm);
        return ResponseEntity.ok(savedFarm);
    }

    // Get all farms
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")
    @Operation(summary = "Get all farms", description = "Accessible by ADMIN, VIEWER, and USER roles")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Farm>> getAllFarms() {
        return ResponseEntity.ok(farmService.getAllFarms());
    }

    // Get a farm by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")    
    @Operation(summary = "Get farm by ID", description = "Accessible by ADMIN, VIEWER, and USER roles")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Farm> getFarmById(@PathVariable Long id) {
        return farmService.getFarmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get a farm by premise ID
    @GetMapping("/premise/{premiseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get farm by premise ID", description = "Accessible by ADMIN, VIEWER, and USER roles")
    public ResponseEntity<Farm> getFarmByPremiseId(@PathVariable String premiseId) {
        Farm farm = farmService.getFarmByPremiseId(premiseId);
        if (farm != null) {
            return ResponseEntity.ok(farm);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a farm
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Farm> updateFarm(@PathVariable Long id, @Valid @RequestBody Farm farmDetails) {
        Farm updatedFarm = farmService.updateFarm(id, farmDetails);
        return ResponseEntity.ok(updatedFarm);
    }


    // Delete a farm
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a farm", description = "Accessible by ADMIN")
    public ResponseEntity<String> deleteFarm(@PathVariable Long id) {
        farmService.deleteFarm(id);
        return ResponseEntity.ok("Farm deleted successfully");
    }
    
    
    
    
}
