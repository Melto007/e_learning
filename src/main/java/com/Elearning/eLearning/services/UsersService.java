package com.Elearning.eLearning.services;

import com.Elearning.eLearning.dto.UserDto;
import com.Elearning.eLearning.models.Profile;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final AuthenticationManager manager;
    private final JWTService jwtService;
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UsersService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UsersRepository usersRepository,
            AuthenticationManager manager,
            JWTService jwtService,
            ProfileRepository profileRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.manager = manager;
        this.jwtService = jwtService;
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
    }

    public Users saveUser(UserDto userDto) {
        if(usersRepository.findByUsername(userDto.username()).isPresent()) {
            throw new RuntimeException("user already exists");
        }

        if(profileRepository.findByEmail(userDto.email()).isPresent()) {
            throw new RuntimeException("email already exists");
        }

        Users user = new Users();
        user.setUsername(userDto.username());
        user.setPassword(passwordEncoder.encode(userDto.password()));

        Role userRoles = roleRepository.findByRole(RoleEnum.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Set.of(userRoles));

        Profile profile = new Profile();
        profile.setEmail(userDto.email());
        user.setProfile(profile);

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
