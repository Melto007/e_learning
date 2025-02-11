package com.Elearning.eLearning.repositories;

import com.Elearning.eLearning.models.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByFirstNameIgnoreCase(String name);
    List<Author> findAllByFirstName(String name);
    List<Author> findByFirstNameContainingIgnoreCase(String name);
    List<Author> findByFirstNameStartsWithIgnoreCase(String name);
    List<Author> findByFirstNameEndsWithIgnoreCase(String name);
    List<Author> findByFirstNameInIgnoreCase(List<String> name);

    @Modifying
    @Transactional
    @Query("update Author a set a.age = :age where a.id = :id")
    void updateAuthor(Integer age, Long id);
}
