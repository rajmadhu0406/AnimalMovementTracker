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

@RestController
@Validated
@RequestMapping("/api/farm")
@Tag(name = "Farm API", description = "APIs for managing farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new farm", description = "Accessible by ADMIN")
    public ResponseEntity<Farm> createFarm(@RequestBody Farm farm) {
        Farm savedFarm = farmService.saveFarm(farm);
        return ResponseEntity.ok(savedFarm);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")
    @Operation(summary = "Get all farms", description = "Accessible by ADMIN, VIEWER, and USER roles")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Farm>> getAllFarms() {
        return ResponseEntity.ok(farmService.getAllFarms());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'USER')")    
    @Operation(summary = "Get farm by ID", description = "Accessible by ADMIN, VIEWER, and USER roles")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Farm> getFarmById(@PathVariable Long id) {
        return farmService.getFarmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Farm> updateFarm(@PathVariable Long id, @RequestBody Farm farmDetails) {
        Farm updatedFarm = farmService.updateFarm(id, farmDetails);
        return ResponseEntity.ok(updatedFarm);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a farm", description = "Accessible by ADMIN")
    public ResponseEntity<String> deleteFarm(@PathVariable Long id) {
        farmService.deleteFarm(id);
        return ResponseEntity.ok("Farm deleted successfully");
    }
    
    
    
    
}
