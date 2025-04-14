package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.Clients;
import javafx.beans.value.ObservableValue;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Clients> getAllClients();
    Clients createClient(Clients client);
    List<Clients> searchClients(String query);
    void deleteClient(Long id);
    Clients findById(Long id);
    Optional<Clients> updateClient(Long id, Clients updatedClient);
}
