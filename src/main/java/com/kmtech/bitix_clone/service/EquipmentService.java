package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Equipment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentService {
    Equipment createEquipment(Equipment equipment);
    List<Equipment> searchEquipment(String query);
    void deleteEquipment(Long id);
    List<Equipment> getAllEquipment();
}
