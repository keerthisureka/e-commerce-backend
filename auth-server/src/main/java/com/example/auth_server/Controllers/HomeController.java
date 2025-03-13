package com.example.auth_server.Controllers;

import com.example.auth_server.entity.User;
import com.example.auth_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> getUser() {
        System.out.println("getting user");
        return this.userService.getAll();
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/getEmailByUserId/{userId}")
    public String getEmailByUserId(@PathVariable String userId) {
        return userService.getEmailByUserId(userId);
    }
}
