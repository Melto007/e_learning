package com.Elearning.eLearning.models;

import com.Elearning.eLearning.utils.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "author_tb")
public class Author extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_name", length = 8)
    private String firstName;

    @Column(name = "l_name", length = 8)
    private String lastName;

    @Column(name = "email_id", unique = true, nullable = false)
    private String email;

    @Min(18)
    private Integer age;

    @ManyToMany(mappedBy = "authors")
    private List<Course> courses;

    private Boolean status;

    @PrePersist
    public void setDefaultValue() {
        if(status == null) {
            status = true;
        }
    }
}
