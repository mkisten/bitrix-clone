package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.Brand;
import com.kmtech.bitix_clone.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByBrand(Brand brand);
    boolean existsByNameAndBrand(String name, Brand brand);
}