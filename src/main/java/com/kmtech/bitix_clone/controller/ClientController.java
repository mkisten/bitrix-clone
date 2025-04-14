package com.kmtech.bitix_clone.controller;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.ClientEquipment;
import com.kmtech.bitix_clone.repository.ClientRepository;
import com.kmtech.bitix_clone.service.ClientEquipmentService;
import com.kmtech.bitix_clone.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ClientEquipmentService clientEquipmentService;
    private final ClientRepository clientRepository;

    // Получение клиента по ID
    @GetMapping("/clients/{id}")
    public ResponseEntity<Clients> getClientById(@PathVariable Long id) {
        try {
            Clients client = clientService.findById(id);
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            log.error("Клиент с ID {} не найден", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Ошибка при получении клиента с ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Создание клиента
    @PostMapping("/clients")
    public ResponseEntity<Clients> createClient(@RequestBody Clients client) {
        try {
            Clients saved = clientService.createClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Ошибка при создании клиента", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Обновление клиента
    @PutMapping("/clients/{id}")
    public ResponseEntity<Clients> updateClient(@PathVariable Long id, @RequestBody Clients updatedClient) {
        try {
            return clientService.updateClient(id, updatedClient)
                    .map(client -> ResponseEntity.ok().body(client))
                    .orElseGet(() -> {
                        log.warn("Клиент с ID {} не найден для обновления", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });
        } catch (Exception e) {
            log.error("Ошибка при обновлении клиента с ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Поиск клиентов
    @GetMapping("/clients/search")
    public ResponseEntity<List<Clients>> searchClients(@RequestParam String query) {
        try {
            List<Clients> clients = clientService.searchClients(query);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            log.error("Ошибка при поиске клиентов с запросом: {}", query, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Удаление клиента
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Клиент с ID {} не найден для удаления", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Ошибка при удалении клиента с ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Получение всех клиентов
    @GetMapping("/get-all-client")
    public ResponseEntity<List<Clients>> getAllClients() {
        try {
            List<Clients> clients = clientRepository.findAll();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            log.error("Ошибка при получении списка всех клиентов", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Получение списка оборудования клиента
    @GetMapping("/client-equipment/{clientId}")
    public ResponseEntity<List<ClientEquipment>> getEquipmentByClient(@PathVariable Long clientId) {
        try {
            Clients client = clientService.findById(clientId);
            List<ClientEquipment> equipment = clientEquipmentService.getEquipmentByClient(client);
            return ResponseEntity.ok(equipment);
        } catch (IllegalArgumentException e) {
            log.error("Клиент с ID {} не найден для получения оборудования", clientId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Ошибка при получении оборудования клиента с ID {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Добавление оборудования клиенту
    @PostMapping("/client-equipment")
    public ResponseEntity<ClientEquipment> createClientEquipment(
            @RequestParam Long clientId,
            @RequestParam Long brandId,
            @RequestParam Long modelId,
            @RequestParam String serialNumber) {
        try {
            Clients client = clientService.findById(clientId);
            ClientEquipment clientEquipment = clientEquipmentService.createClientEquipment(client, brandId, modelId, serialNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(clientEquipment);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при добавлении оборудования для клиента с ID {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Ошибка при добавлении оборудования для клиента с ID {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Удаление оборудования клиента
    @DeleteMapping("/client-equipment/{id}")
    public ResponseEntity<Void> deleteClientEquipment(@PathVariable Long id) {
        try {
            clientEquipmentService.deleteClientEquipment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Ошибка при удалении оборудования с ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}