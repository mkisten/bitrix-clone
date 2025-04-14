package com.kmtech.bitix_clone.service;

import com.kmtech.bitix_clone.model.Request;
import com.kmtech.bitix_clone.repository.RequestRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final RequestRepository requestRepository;

    public String generateMonthlyReport(Long engineerId, String yearMonth) {
        LocalDateTime start = LocalDateTime.parse(yearMonth + "-01T00:00:00");
        LocalDateTime end = start.plusMonths(1);

        List<Request> requests = requestRepository.findByEngineerIdAndCreatedAtBetween(engineerId, start, end);

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            String[] header = {"Request ID", "Type", "Service Cost", "Transport Cost", "Total Cost", "Completed At"};
            csvWriter.writeNext(header);

            for (Request request : requests) {
                String[] row = {
                        request.getId().toString(),
                        request.getType().name(),
                        request.getServiceCost().toString(),
                        request.getTransportCost().toString(),
                        request.getTotalCost().toString(),
                        request.getCompletedAt().toString()
                };
                csvWriter.writeNext(row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report", e);
        }

        return stringWriter.toString();
    }
}