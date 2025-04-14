package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.Equipment;
import com.kmtech.bitix_clone.model.Request;
import com.kmtech.bitix_clone.model.RequestType;
import com.kmtech.bitix_clone.repository.AnnualMaintenanceRepository;
import com.kmtech.bitix_clone.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private AnnualMaintenanceRepository annualMaintenanceRepository; // Добавляем мок

    @InjectMocks
    private RequestServiceImpl requestService;

    private Request request;

    @BeforeEach
    public void setUp() {
        request = new Request();
        request.setClient(new Clients());
        request.setEquipment(new Equipment());
    }

    @Test
    public void testCreateRequestWithMaintenanceType() {
        // Настраиваем поведение моков
        request.setType(RequestType.MAINTENANCE);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(annualMaintenanceRepository.findAll()).thenReturn(Collections.emptyList()); // Мокаем findAll()

        // Вызываем метод
        Request result = requestService.createRequest(request);
        assertNotNull(result);
    }

    @Test
    public void testCreateRequestWithNullType() {
        request.setType(null);
        assertThrows(IllegalArgumentException.class, () -> requestService.createRequest(request));
    }

    @Test
    public void testIsFirstMaintenanceWhenNoPreviousMaintenances() {
        request.setType(RequestType.MAINTENANCE);
        when(annualMaintenanceRepository.findAll()).thenReturn(Collections.emptyList());

        boolean result = requestService.isFirstMaintenance(request);
        assertTrue(result); // Предполагаем, что метод возвращает true, если нет предыдущих обслуживаний
    }
}