package com.kmtech.bitix_clone.controller;

import com.kmtech.bitix_clone.model.Request;
import com.kmtech.bitix_clone.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/requests")
    public Request createRequest(@RequestBody Request request) {
        return requestService.createRequest(request);
    }

    @PutMapping("/requests/{id}/assign")
    public void assignRequest(@PathVariable Long id, @RequestParam Long engineerId) {
        requestService.assignRequest(id, engineerId);
    }

    @PutMapping("/requests/{id}/complete")
    public void completeRequest(@PathVariable Long id,
                                @RequestParam BigDecimal serviceCost,
                                @RequestParam BigDecimal transportCost) {
        requestService.completeRequest(id, serviceCost, transportCost);
    }

    @GetMapping("/get-all-requests")
    public List<Request> getAllRequests() {
        return requestService.findAll();
    }
}
