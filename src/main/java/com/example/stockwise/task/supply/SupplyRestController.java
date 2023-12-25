package com.example.stockwise.task.supply;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouses/{id}/supplies")
public class SupplyRestController {

    private final SupplyService supplyService;

    @PostMapping()
    public ResponseEntity<?> addSupply(@PathVariable Long id, @RequestBody Supply supply) {
        try {
            supplyService.addSupply(supply, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }


    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            supplyService.completeSupply(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            supplyService.deleteSupply(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }

}