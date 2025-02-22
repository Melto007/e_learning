package com.Elearning.eLearning.services;

import com.Elearning.eLearning.dto.UserDto;
import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.repositories.ProfileRepository;
import com.Elearning.eLearning.repositories.profile.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final AuthenticationManager manager;
    private final JWTService jwtService;
    private final ProfileRepository profileRepository;
    private final UsersMapper usersMapper;
    private final UserService userService;

    @Autowired
    public UsersService(
            UsersRepository usersRepository,
            AuthenticationManager manager,
            JWTService jwtService,
            ProfileRepository profileRepository,
            UsersMapper usersMapper,
            UserService userService
    ) {
        this.usersRepository = usersRepository;
        this.manager = manager;
        this.jwtService = jwtService;
        this.profileRepository = profileRepository;
        this.usersMapper = usersMapper;
        this.userService = userService;
    }

    public Users saveUser(UserDto userDto) {
        if(usersRepository.findByUsername(userDto.username()).isPresent()) {
            throw new RuntimeException("user already exists");
        }

        if(profileRepository.findByEmail(userDto.email()).isPresent()) {
            throw new RuntimeException("email already exists");
        }

        var user = usersMapper.toUsers(userDto);
        return usersRepository.save(user);
    }

    public Map<String, Object> verify(Users users) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        users.getUsername(),
                        users.getPassword()
                )
        );

        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(users.getUsername());

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String firstRole = roles.stream().findFirst().orElse("USER");

            Map<String, Object> response = new HashMap<>();
            response.put("access_token", token);
            response.put("roles", firstRole);
            return response;
        }

        throw new ArithmeticException("Unauthenticated");
    }

    public List<Users> getUsers() {
        return usersRepository.findAllWithProfile();
    }

    public Map<String, Object> getToken(String token, String roles) {
        String username = jwtService.extractUsername(token);

        if(username != null) {
            String refresh_token = jwtService.generateRefreshToken(username);
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", token);
            response.put("refresh_token", refresh_token);
            response.put("roles", roles);
            return response;
        }
        return null;
    }

    public String checkRefreshToken(String token) {
        String username = jwtService.extractUsername(token);

        if(username != null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)) {
                return jwtService.generateToken(username);
            } else {
                return null;
            }
        }
        return null;
    }
}
