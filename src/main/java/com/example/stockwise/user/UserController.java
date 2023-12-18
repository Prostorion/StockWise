package com.example.stockwise.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

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
        return "warehouse/register_worker";
    }

    @GetMapping("api/v1/user")
    public String userInfo(Model model) throws Exception {
        model.addAttribute(userService.getUser());
        return "user_info";
    }

    @GetMapping("api/v1/user/edit")
    public String userInfoEdit(Model model) throws Exception {
        model.addAttribute(userService.getUser());
        return "user_info_edit";
    }

    @PutMapping("api/v1/user")
    @ResponseBody
    public ResponseEntity<?> editCurrentUser(@RequestBody User user) {
        try {
            userService.updateCurrentUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
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

    @DeleteMapping("/api/v1/warehouses/{id}/users/{user_id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @PathVariable Long user_id) {
        try {
            userService.deleteWorker(user_id, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }


}
