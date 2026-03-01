package com.quizplatform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "quiz_id")
        private Long id;

        @Column(name = "title", nullable = false)
        private String title;

        @Column(name = "description")
        private String description;

        @Column(name = "category")
        private String category;

        @Column(name = "created_by")
        private Integer createdBy;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Question> questions = new java.util.ArrayList<>();
}

