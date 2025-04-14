package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.ClientEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientEquipmentRepository extends JpaRepository<ClientEquipment, Long> {
    List<ClientEquipment> findByClient(Clients client);
}