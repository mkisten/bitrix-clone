package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Brand;
import com.kmtech.bitix_clone.model.ClientEquipment;
import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.Model;
import com.kmtech.bitix_clone.repository.ClientEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientEquipmentServiceImpl implements ClientEquipmentService {
    private final ClientEquipmentRepository clientEquipmentRepository;
    private final BrandService brandService;
    private final ModelService modelService;

    public ClientEquipment createClientEquipment(Clients client, Long brandId, Long modelId, String serialNumber) {
        Brand brand = brandService.getAllBrands().stream()
                .filter(b -> b.getId().equals(brandId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Марка не найдена"));

        Model model = modelService.getAllModels().stream()
                .filter(m -> m.getId().equals(modelId) && m.getBrand().getId().equals(brandId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Модель не найдена или не соответствует марке"));

        ClientEquipment clientEquipment = new ClientEquipment();
        clientEquipment.setClient(client);
        clientEquipment.setBrand(brand);
        clientEquipment.setModel(model);
        clientEquipment.setSerialNumber(serialNumber);

        return clientEquipmentRepository.save(clientEquipment);
    }

    public List<ClientEquipment> getEquipmentByClient(Clients client) {
        return clientEquipmentRepository.findByClient(client);
    }

    public void deleteClientEquipment(Long id) {
        clientEquipmentRepository.deleteById(id);
    }
}
