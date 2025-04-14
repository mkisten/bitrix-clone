package com.kmtech.bitix_clone.controller;

import com.kmtech.bitix_clone.model.Model;
import com.kmtech.bitix_clone.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @PostMapping("/models")
    public Model createModel(@RequestBody Model model) {
        return modelService.createModel(model);
    }

    @GetMapping("/models/by-brand/{brandId}")
    public List<Model> getModelsByBrand(@PathVariable Long brandId) {
        return modelService.getModelsByBrand(brandId);
    }

    @GetMapping("/models")
    public List<Model> getAllModels() {
        return modelService.getAllModels();
    }

    @DeleteMapping("/models/{id}")
    public void deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
    }
}