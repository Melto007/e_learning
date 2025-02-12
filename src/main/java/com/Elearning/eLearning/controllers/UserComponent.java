package com.Elearning.eLearning.controllers;

import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.reponse.ApiResponse;
import com.Elearning.eLearning.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("${api.prefix}")
public class UserComponent {
    private final UsersService usersService;

    public UserComponent(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> userRequest(HttpServletRequest request) {
       try {
           return ResponseEntity.status(200).body(new ApiResponse<>(200, "Hello World", request.getSession().getId()));
       } catch (HttpClientErrorException.Unauthorized e) {
           return ResponseEntity.status(401).body(new ApiResponse<>(401, e.getMessage(), null));
       }
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public String students() {
        return "Students";
    }

    @PostMapping("/register")
    public Users saveRequest(@RequestBody Users users) {
        return usersService.saveUser(users);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Users users) {
        return usersService.verify(users);
    }
}
