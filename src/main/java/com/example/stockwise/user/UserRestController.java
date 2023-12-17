package com.example.stockwise.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/api/rest/users")
@AllArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<?> currentUser() {
        //fixme: get()
        return new ResponseEntity<>(userService.getUser().get(), HttpStatusCode.valueOf(200));
    }
}
