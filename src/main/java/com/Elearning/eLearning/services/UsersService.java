package com.Elearning.eLearning.services;

import com.Elearning.eLearning.dto.UserDto;
import com.Elearning.eLearning.models.Role;
import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.repositories.ProfileRepository;
import com.Elearning.eLearning.repositories.RoleRepository;
import com.Elearning.eLearning.repositories.UserRepository;
import com.Elearning.eLearning.repositories.profile.UsersRepository;
import com.Elearning.eLearning.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final AuthenticationManager manager;
    private final JWTService jwtService;
    private final ProfileRepository profileRepository;
    private final UsersMapper usersMapper;

    @Autowired
    public UsersService(
            UsersRepository usersRepository,
            AuthenticationManager manager,
            JWTService jwtService,
            ProfileRepository profileRepository,
            UsersMapper usersMapper
    ) {
        this.usersRepository = usersRepository;
        this.manager = manager;
        this.jwtService = jwtService;
        this.profileRepository = profileRepository;
        this.usersMapper = usersMapper;
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

    public String verify(Users users) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        users.getUsername(),
                        users.getPassword()
                )
        );

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getUsername());
        }

        throw new ArithmeticException("Unauthenticated");
    }

    public List<Users> getUsers() {
        return usersRepository.findAllWithProfile();
    }
}
