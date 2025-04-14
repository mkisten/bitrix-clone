package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.AnnualMaintenance;
import com.kmtech.bitix_clone.model.Request;
import com.kmtech.bitix_clone.model.RequestType;
import com.kmtech.bitix_clone.repository.AnnualMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnualMaintenanceScheduler {
    private final RequestService requestService;
    private final AnnualMaintenanceRepository annualMaintenanceRepository;

    @Scheduled(cron = "0 0 1 * * ?") // Каждый день в 01:00
    public void createAnnualMaintenanceRequests() {
        List<AnnualMaintenance> maintenances = annualMaintenanceRepository.findAll();
        LocalDate today = LocalDate.now();

        for (AnnualMaintenance maintenance : maintenances) {
            LocalDate firstMaintenanceDate = maintenance.getFirstMaintenanceDate();
            // Проверяем, является ли текущая дата годовщиной первого ТО
            if (isAnniversary(firstMaintenanceDate, today)) {
                Request request = new Request();
                request.setClient(maintenance.getClients());
                request.setEquipment(maintenance.getEquipment());
                request.setType(RequestType.MAINTENANCE);
                request.setDescription("Annual maintenance for " + maintenance.getEquipment().getModel());
                requestService.createRequest(request);
            }
        }
    }

    private boolean isAnniversary(LocalDate firstDate, LocalDate today) {
        // Проверяем, является ли текущая дата годовщиной (с учетом високосных годов)
        return firstDate.getDayOfMonth() == today.getDayOfMonth() &&
                firstDate.getMonth() == today.getMonth() &&
                firstDate.plusYears(today.getYear() - firstDate.getYear()).isBefore(today.plusDays(1));
    }
}
