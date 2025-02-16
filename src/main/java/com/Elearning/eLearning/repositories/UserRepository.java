package com.Elearning.eLearning.repositories;

import com.Elearning.eLearning.models.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("select u from Users u WHERE u.username = :username")
    Optional<Users> findByUsername(@Param("username") String username);
}
