package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.AnnualMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualMaintenanceRepository extends JpaRepository<AnnualMaintenance, Long> {
    List<AnnualMaintenance> findByEquipmentId(Long equipmentId);
}
