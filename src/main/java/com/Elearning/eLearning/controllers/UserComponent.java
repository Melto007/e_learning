package com.Elearning.eLearning.controllers;

import com.Elearning.eLearning.dto.UserDto;
import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.reponse.ApiResponse;
import com.Elearning.eLearning.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}")
@Validated
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

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String students() {
        return "admin";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> saveRequest(@Valid @RequestBody UserDto userDto) {
        Users users1 = usersService.saveUser(userDto);
        if(users1 != null) {
            return ResponseEntity.status(201)
                    .body(new ApiResponse<>(201, "user registered success", null));
        } else {
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(400, "user couldn't saved", null));
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

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getUsers() {
        List<Users> users = usersService.getUsers();
        return ResponseEntity.status(200).body(new ApiResponse<>(200, "user data", users));
    }
}
