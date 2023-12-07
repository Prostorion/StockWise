package com.example.stockwise.warehouse;

import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    UserService userService;
    WarehouseService warehouseService;

    public WarehouseController(UserService userService, WarehouseService warehouseService) {
        this.userService = userService;
        this.warehouseService = warehouseService;
    }

    @GetMapping()
    public String warehouses(Model model) {
        User user = userService.getUser().orElseThrow(() -> new UsernameNotFoundException("there is no user"));
        model.addAttribute(user);
        Set<Warehouse> warehouses = warehouseService.getUserWarehouses(user);
        model.addAttribute(warehouses);
        return "warehouses";
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<?> addWarehouse(@RequestBody Warehouse warehouse) {
        try {
            warehouseService.addWarehouse(warehouse);
            return ResponseEntity.ok("warehouse added");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        try {
            warehouseService.deleteWarehouseById(id);
            return ResponseEntity.ok("warehouse added");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    @GetMapping("/{id}")
    public String warehouse(@PathVariable Long id, Model model) throws Exception {
        User user = userService.getUser().orElseThrow(() -> new UsernameNotFoundException("there is no user"));
        model.addAttribute(user);
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        model.addAttribute(warehouse);
        return "warehouse_id";
    }

    @GetMapping("/{id}/history")
    public String warehousesSettings(@PathVariable Long id, Model model) throws Exception {
        User user = userService.getUser().orElseThrow(() -> new UsernameNotFoundException("there is no user"));
        model.addAttribute(user);
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        model.addAttribute(warehouse);
        return "warehouse_history";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateWarehouse(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        try {
            warehouseService.updateWarehouse(id, warehouse);
            return ResponseEntity.ok("warehouse updated");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
