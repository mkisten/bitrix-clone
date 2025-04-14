package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEngineerIdAndCreatedAtBetween(Long engineerId, LocalDateTime start, LocalDateTime end);
}
