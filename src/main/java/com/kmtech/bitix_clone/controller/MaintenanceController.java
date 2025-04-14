package com.kmtech.bitix_clone.controller;

import com.kmtech.bitix_clone.model.AnnualMaintenance;
import com.kmtech.bitix_clone.repository.AnnualMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@CrossOrigin(origins = "http://localhost:3000")
public class MaintenanceController {

    @Autowired
    private AnnualMaintenanceRepository maintenanceRepository;

    @GetMapping
    public List<AnnualMaintenance> getMaintenancesByEquipmentId(@RequestParam Long equipmentId) {
        return maintenanceRepository.findByEquipmentId(equipmentId);
    }
}