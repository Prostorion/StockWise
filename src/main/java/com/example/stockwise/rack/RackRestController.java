package com.example.stockwise.rack;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/warehouses/{id}/racks")
@RequiredArgsConstructor
public class RackRestController {

    private final RackService rackService;

    @GetMapping("/{rackNumber}")
    public ResponseEntity<?> getRack(@PathVariable("id") Long id, @PathVariable("rackNumber") Long rackNumber) {
        try {
            Rack rack = rackService.getRack(id, rackNumber);
            return new ResponseEntity<>(rack.getItems(), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
        }
    }

}
