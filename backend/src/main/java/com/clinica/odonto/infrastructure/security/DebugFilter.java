package com.clinica.odonto.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1) // Execute before JWT filter
public class DebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");
        
        // Log only for problematic endpoints
        if (requestURI.contains("/periodo")) {
            System.out.println("=== DEBUG FILTER ===");
            System.out.println("URI: " + requestURI);
            System.out.println("Method: " + method);
            System.out.println("Auth Header: " + (authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 20)) + "..." : "null"));
            System.out.println("Query String: " + request.getQueryString());
            System.out.println("Content Type: " + request.getContentType());
            System.out.println("========================");
        }
        
        filterChain.doFilter(request, response);
    }
}