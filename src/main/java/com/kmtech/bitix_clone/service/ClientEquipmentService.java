package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.ClientEquipment;
import com.kmtech.bitix_clone.model.Clients;

import java.util.List;

public interface ClientEquipmentService {
    ClientEquipment createClientEquipment(Clients client, Long brandId, Long modelId, String serialNumber);
    List<ClientEquipment> getEquipmentByClient(Clients client);
    void deleteClientEquipment(Long id);
}
