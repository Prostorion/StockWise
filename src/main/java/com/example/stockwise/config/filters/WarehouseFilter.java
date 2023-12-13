package com.example.stockwise.config.filters;


import com.example.stockwise.services.MainService;
import com.example.stockwise.user.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashSet;

@Component
public class WarehouseFilter extends GenericFilterBean {

    private final UserService userService;
    private final MainService mainService;
    public WarehouseFilter(UserService userService, MainService mainService) {
        this.userService = userService;
        this.mainService = mainService;
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

            // Check if the requested warehouse is related to the current user
            //FIXME: change to warehouseService
            if (!userService.isWarehouseAccessibleByUser(currentUser.getUsername(), requestedWarehouseId)) {
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