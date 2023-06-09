package com.sv.simpleapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String name;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Department department;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "employee")
    private List<Review> reviews;

    @OneToMany(mappedBy = "author")
    private List<Submission> submissions;
}
