package com.Elearning.eLearning.models;

import com.Elearning.eLearning.utils.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Section extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "total_order")
    private Integer order;

    @ManyToOne
    @JoinColumn(
            name="course_id"
    )
    private Course course;

    @OneToMany(mappedBy = "section")
    private List<Lecture> lectures;

    private Boolean status;

    @PrePersist
    public void getDefaultValue() {
        if(status == null) {
            status = true;
        }
    }
}
