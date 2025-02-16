package com.Elearning.eLearning.repositories;

import com.Elearning.eLearning.models.Role;
import com.Elearning.eLearning.utils.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(RoleEnum name);
}
