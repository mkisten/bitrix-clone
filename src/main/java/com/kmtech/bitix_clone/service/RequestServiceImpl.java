package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.*;
import com.kmtech.bitix_clone.repository.AnnualMaintenanceRepository;
import com.kmtech.bitix_clone.repository.EngineerRepository;
import com.kmtech.bitix_clone.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EngineerRepository engineerRepository;
    private final AnnualMaintenanceRepository annualMaintenanceRepository;
    private final EmailServiceImpl emailService;

    @Override
    public Request createRequest(Request request) {

        if (request.getType() == null) {
            log.warn("Request type is null for request: {}", request);
            throw new IllegalArgumentException("Request type must not be null");
        }

        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.NEW);
        Request savedRequest = requestRepository.save(request);

        // Проверяем, что тип заявки - MAINTENANCE и это первое ТО
        if (request.getType() != null && request.getType() == RequestType.MAINTENANCE && isFirstMaintenance(request)) {
            AnnualMaintenance annualMaintenance = new AnnualMaintenance();
            annualMaintenance.setClients(request.getClient());
            annualMaintenance.setEquipment(request.getEquipment());
            annualMaintenance.setFirstMaintenanceDate(LocalDate.now());
            annualMaintenanceRepository.save(annualMaintenance);
        }

        if (request.getEngineer() != null) {
            assignRequest(savedRequest.getId(), request.getEngineer().getId());
        }
        return savedRequest;
    }

    boolean isFirstMaintenance(Request request) {
        // Проверяем, нет ли уже записи для этого клиента и оборудования
        return annualMaintenanceRepository.findAll().stream()
                .noneMatch(am -> am.getClients().getId().equals(request.getClient().getId()) &&
                        am.getEquipment().getId().equals(request.getEquipment().getId()));
    }

    @Override
    public void assignRequest(Long requestId, Long engineerId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        Engineer engineer = engineerRepository.findById(engineerId)
                .orElseThrow(() -> new IllegalArgumentException("Engineer not found"));

        request.setEngineer(engineer);
        request.setStatus(RequestStatus.IN_PROGRESS);
        requestRepository.save(request);

        emailService.sendNotification(engineer.getEmail(), "New request assigned",
                "Request #" + requestId + ": " + request.getDescription());
    }

    @Override
    public void completeRequest(Long requestId, BigDecimal serviceCost, BigDecimal transportCost) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setServiceCost(serviceCost);
        request.setTransportCost(transportCost);
        request.setTotalCost(serviceCost.add(transportCost));
        request.setStatus(RequestStatus.COMPLETED);
        request.setCompletedAt(LocalDateTime.now());
        requestRepository.save(request);
    }

    @Override
    public List<Request> findAll() {
        return requestRepository.findAll();
    }
}
