package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineerRepository extends JpaRepository<Engineer, Long> {
    Engineer findByPhone(String phone);
}
