package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Brand;

import java.util.List;

public interface BrandService {
    Brand createBrand(Brand brand);
    List<Brand> getAllBrands();
    void deleteBrand(Long id);
}
