package com.example.stockwise.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    public UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/v1/users/new")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/api/v1/users")
    @ResponseBody
    public ResponseEntity<?> registerManager(@RequestBody User user) {
        try {
            userService.addManager(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/warehouses/{id}/users/new")
    public String registerWorker(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(userService.getUser().orElseThrow(() -> new Exception("user not found")));
        return "warehouse/register_worker";
    }

    @PostMapping("/api/v1/warehouses/{id}/users")
    @ResponseBody
    public ResponseEntity<?> registerWorker(@PathVariable Long id, @RequestBody User user) {
        try {
            userService.addWorkerToWarehouse(user, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }


}
