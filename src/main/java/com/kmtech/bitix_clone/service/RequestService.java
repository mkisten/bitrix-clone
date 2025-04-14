package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Request;

import java.math.BigDecimal;
import java.util.List;

public interface RequestService {
    Request createRequest(Request request);
    void assignRequest(Long requestId, Long engineerId);
    void completeRequest(Long requestId, BigDecimal serviceCost, BigDecimal transportCost);
    List<Request> findAll();
}
