package com.Elearning.eLearning.services;

import com.Elearning.eLearning.models.UserPrinciple;
import com.Elearning.eLearning.models.Users;
import com.Elearning.eLearning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);

        if(users == null) {
            System.out.print("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrinciple(users);
    }
}
