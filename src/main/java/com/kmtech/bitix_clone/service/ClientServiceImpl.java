package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public Clients createClient(Clients client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Clients> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Clients> searchClients(String query) {
        return clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Clients findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));
    }

    @Override
    public Optional<Clients> updateClient(Long id, Clients updatedClient) {
        return clientRepository.findById(id).map(client -> {
            client.setFirstName(updatedClient.getFirstName());
            client.setLastName(updatedClient.getLastName());
            client.setSurname(updatedClient.getSurname());
            client.setEmail(updatedClient.getEmail());
            client.setPhone(updatedClient.getPhone());
            return clientRepository.save(client);
        });
    }
}
