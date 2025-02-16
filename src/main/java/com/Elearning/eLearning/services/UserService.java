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
        return userRepository.findByUsername(username)
                .map(UserPrinciple::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
