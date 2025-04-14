//package com.kmtech.bitix_clone.controller;
//
//import com.kmtech.bitix_clone.model.ClientEquipment;
//import com.kmtech.bitix_clone.service.ClientEquipmentService;
//import com.kmtech.bitix_clone.service.ClientService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class ClientEquipmentController {
//
//    private final ClientEquipmentService clientEquipmentService;
//    private final ClientService clientService;
//
//    @PostMapping("/client-equipment")
//    public ClientEquipment createClientEquipment(
//            @RequestParam Long clientId,
//            @RequestParam Long brandId,
//            @RequestParam Long modelId,
//            @RequestParam String serialNumber) {
//        return clientEquipmentService.createClientEquipment(
//                clientService.findById(clientId), brandId, modelId, serialNumber);
//    }
//
//    @GetMapping("/client-equipment/{clientId}")
//    public List<ClientEquipment> getEquipmentByClient(@PathVariable Long clientId) {
//        return clientEquipmentService.getEquipmentByClient(clientService.findById(clientId));
//    }
//
//    @DeleteMapping("/client-equipment/{id}")
//    public void deleteClientEquipment(@PathVariable Long id) {
//        clientEquipmentService.deleteClientEquipment(id);
//    }
//}