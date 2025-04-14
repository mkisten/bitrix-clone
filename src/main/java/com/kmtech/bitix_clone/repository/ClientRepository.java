package com.kmtech.bitix_clone.repository;

import com.kmtech.bitix_clone.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Long> {
//    @Query("SELECT c FROM Clients c WHERE " +
//            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(c.city) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(c.street) LIKE LOWER(CONCAT('%', :query, '%'))")
//    List<Clients> searchClients(String query);

    List<Clients> findAll();

    List<Clients> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}