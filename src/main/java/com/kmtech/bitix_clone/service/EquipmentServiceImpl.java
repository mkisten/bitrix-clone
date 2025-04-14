package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Equipment;
import com.kmtech.bitix_clone.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    // Создание оборудования
    @Override
    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Поиск оборудования
    @Override
    public List<Equipment> searchEquipment(String query) {
        return equipmentRepository.findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(query, query);
    }

    // Удаление оборудования
    @Override
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    // Получение всех записей оборудования
    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }
}
