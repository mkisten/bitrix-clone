package com.kmtech.bitix_clone.controller;

import com.kmtech.bitix_clone.model.Equipment;
import com.kmtech.bitix_clone.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @PostMapping("/equipment")
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @GetMapping("/equipment/search")
    public List<Equipment> searchEquipment(@RequestParam String query) {
        return equipmentService.searchEquipment(query);
    }

    @DeleteMapping("/equipment/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }

    @GetMapping("/get-all-equipment")
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }
}