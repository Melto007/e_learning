package com.Elearning.eLearning.services;

import com.Elearning.eLearning.dto.UserDto;
import com.Elearning.eLearning.models.Profile;
import com.Elearning.eLearning.models.Role;
import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.repositories.RoleRepository;
import com.Elearning.eLearning.utils.RoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UsersMapper {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UsersMapper(
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Users toUsers(UserDto userDto) {
        if(userDto == null) {
            throw new NullPointerException("User dto should not be empty");
        }

        Users users = new Users();
        users.setUsername(userDto.username());
        users.setPassword(passwordEncoder.encode(userDto.password()));

        Role userRoles = roleRepository.findByRole(RoleEnum.USER)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
        users.setRoles(Set.of(userRoles));

        Profile profile = new Profile();
        profile.setEmail(userDto.email());
        users.setProfile(profile);
        return users;
    }
}
