package com.Elearning.eLearning.controllers;

import com.Elearning.eLearning.dto.TokenRequestDto;
import com.Elearning.eLearning.dto.UserDto;
import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.reponse.ApiResponse;
import com.Elearning.eLearning.services.JWTService;
import com.Elearning.eLearning.services.UserService;
import com.Elearning.eLearning.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JWTService jwtService;
    private final UserService userService;

    public UserComponent(UsersService usersService, JWTService jwtService, UserService userService) {
        this.usersService = usersService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> userRequest(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Unauthorized Access!", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse<>(200, "Hello World", null));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<ApiResponse<?>> getCsrfToken(@RequestBody TokenRequestDto tokenRequestDto) {
        try {
            String refreshToken = tokenRequestDto.token();
            String newAccessToken = usersService.checkRefreshToken(refreshToken);
            if(newAccessToken != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("access_token", newAccessToken);
                return ResponseEntity.status(200).body(new ApiResponse<>(200, "success", response));
            } else {
                return ResponseEntity.status(400).body(new ApiResponse<>(200, "failed", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse<>(200, e.getMessage(), null));
        }
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
            Map<String, Object> object = usersService.verify(users);
            String token = (String) object.get("access_token");
            String roles = (String) object.get("roles");
            Map<String, Object> response = usersService.getToken(token, roles);

            if(response != null) {
                return ResponseEntity.status(200).body(new ApiResponse<>(200, "login success", response));
            }
            return ResponseEntity.status(401).body(new ApiResponse<>(200, "Invalid Credential", null));
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
