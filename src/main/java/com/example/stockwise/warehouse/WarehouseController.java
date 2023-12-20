package com.example.stockwise.warehouse;

import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final UserService userService;
    private final WarehouseService warehouseService;

    @GetMapping()
    public String warehouses(Model model) throws Exception {
        User user = userService.getUser();
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
        return "warehouse/warehouse_id";
    }

    @GetMapping("/{id}/users")
    public String warehouseUsers(@PathVariable Long id, Model model) throws Exception {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        model.addAttribute(warehouse);
        model.addAttribute(userService.getWarehouseUsers(id));
        return "warehouse/users";
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

    @GetMapping("/rest/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<>(warehouseService.getWarehouseById(id), HttpStatusCode.valueOf(200));
    }
}
