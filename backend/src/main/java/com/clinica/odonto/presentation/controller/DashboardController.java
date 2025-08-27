package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.DashboardMetricsResponse;
import com.clinica.odonto.application.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<DashboardMetricsResponse> obterMetricas() {
        try {
            DashboardMetricsResponse metrics = dashboardService.obterMetricas();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}