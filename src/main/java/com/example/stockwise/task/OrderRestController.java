package com.example.stockwise.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouses/{id}/orders")
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> addOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            orderService.addOrder(order, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            orderService.deleteOrder(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getOrder(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            orderService.getOrderByIdAndWarehouseId(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            orderService.completeOrder(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }
}
