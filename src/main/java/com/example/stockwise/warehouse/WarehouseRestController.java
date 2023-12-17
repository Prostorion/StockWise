package com.example.stockwise.warehouse;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/api/rest/warehouses")
@AllArgsConstructor
public class WarehouseRestController {

    private final WarehouseService warehouseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<>(warehouseService.getWarehouseById(id), HttpStatusCode.valueOf(200));
    }
}
