package com.clinica.odonto.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/simple")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Map<String, String>> testSimple() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint without parameters works");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-param")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Map<String, String>> testWithParam(@RequestParam String testParam) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint with parameter works");
        response.put("receivedParam", testParam);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-optional-param")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Map<String, String>> testWithOptionalParam(@RequestParam(required = false) String optionalParam) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint with optional parameter works");
        response.put("receivedParam", optionalParam != null ? optionalParam : "null");
        return ResponseEntity.ok(response);
    }
}