package com.Elearning.eLearning.controllers;

import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.reponse.ApiResponse;
import com.Elearning.eLearning.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}")
public class UserComponent {
    private final UsersService usersService;

    public UserComponent(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> userRequest(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Unauthorized Access!", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse<>(200, "Hello World", null));
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
    public ResponseEntity<ApiResponse<?>> saveRequest(@RequestBody Users users) {
        try {
            if(users.getUsername() == null || users.getUsername().isBlank()) {
                return ResponseEntity.status(400)
                        .body(new ApiResponse<>(400, "username is required", null));
            } else if(users.getPassword() == null || users.getPassword().isBlank()) {
                return ResponseEntity.status(400)
                        .body(new ApiResponse<>(400, "password is required", null));
            } else {
                Users users1 = usersService.saveUser(users);
                if(users1 != null) {
                    return ResponseEntity.status(201)
                            .body(new ApiResponse<>(201, "user registered success", null));
                } else {
                    return ResponseEntity.status(400)
                            .body(new ApiResponse<>(400, "user couldn't saved", null));
                }
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(409)
                    .body(new ApiResponse<>(409, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody Users users) {
        try {
            String token = usersService.verify(users);
            return ResponseEntity.status(200).body(new ApiResponse<>(200, "login success", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new ApiResponse<>(200, e.getMessage(), null));
        }
    }
}
