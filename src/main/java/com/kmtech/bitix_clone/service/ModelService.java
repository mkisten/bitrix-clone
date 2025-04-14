package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Model;

import java.util.List;

public interface ModelService {
    Model createModel(Model model);
    List<Model> getModelsByBrand(Long brandId);
    List<Model> getAllModels();
    void deleteModel(Long id);
}
