package com.example.stockwise.config.filters;


import com.example.stockwise.main.MainService;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

@Component
public class WarehouseFilter extends GenericFilterBean {

    private final MainService mainService;

    private final WarehouseRepository warehouseRepository;

    public WarehouseFilter(MainService mainService, WarehouseRepository warehouseRepository) {
        this.mainService = mainService;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;
        // Extract the requested URL
        String requestURI = getRequestURI(request);

        // Check if the URL matches the expected pattern "/api/v1/warehouses/{id}"
        if (requestURI.matches("/api/v1/warehouses/\\d+(/.*)?")) {
            // Get the current user from the security context
            UserDetails currentUser = mainService.getUserDetails().orElse(new User("ex", "ex", new HashSet<>()));

            // Extract the warehouse ID from the URL
            String[] parts = requestURI.split("/");
            Long requestedWarehouseId = Long.parseLong(parts[4]);
            Optional<Warehouse> warehouse = warehouseRepository.findById(requestedWarehouseId);

            if (warehouse.isEmpty() ||
                    warehouseRepository.findAllByUserUsername(currentUser.getUsername()).contains(warehouse.get())
            ) {
                if (request.getMethod().equals("GET")) {
                    response.sendRedirect("/error/403");
                } else {
                    response.getWriter().write("Access to request is forbidden");
                    response.setStatus(403);
                }
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private String getRequestURI(ServletRequest request) {
        return ((HttpServletRequest) request).getRequestURI();
    }
}