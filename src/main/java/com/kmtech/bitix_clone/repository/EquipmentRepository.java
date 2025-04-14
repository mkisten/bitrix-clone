package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(String brand, String model);
}
