package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Brand;
import com.kmtech.bitix_clone.model.Model;
import com.kmtech.bitix_clone.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final BrandService brandService;

    public Model createModel(Model model) {
        if (modelRepository.existsByNameAndBrand(model.getName(), model.getBrand())) {
            throw new IllegalArgumentException("Модель с таким названием для этой марки уже существует");
        }
        return modelRepository.save(model);
    }

    public List<Model> getModelsByBrand(Long brandId) {
        Brand brand = brandService.getAllBrands().stream()
                .filter(b -> b.getId().equals(brandId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Марка не найдена"));
        return modelRepository.findByBrand(brand);
    }

    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }

    public void deleteModel(Long id) {
        modelRepository.deleteById(id);
    }
}
