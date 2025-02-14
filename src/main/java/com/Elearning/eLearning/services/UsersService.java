package com.Elearning.eLearning.services;

import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.repositories.UserRepository;
import com.Elearning.eLearning.repositories.profile.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final AuthenticationManager manager;
    private final JWTService jwtService;

    @Autowired
    public UsersService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UsersRepository usersRepository,
            AuthenticationManager manager,
            JWTService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    public Users saveUser(Users users) {
        if(usersRepository.findByUsername(users.getUsername()).isPresent()) {
            throw new RuntimeException("user already exists");
        }
        Users user = new Users();
        user.setPassword(users.getUsername());
        user.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepository.save(user);
    }

    public String verify(Users users) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        users.getUsername(), users.getPassword()
                )
        );

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getUsername());
        }

        throw new ArithmeticException("Unauthenticated");
    }
}
