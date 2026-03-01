package com.quizplatform.repository;

import com.quizplatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    java.util.List<Quiz> findByTitleContainingIgnoreCase(String title);
    java.util.List<Quiz> findByCategoryIgnoreCase(String category);
    java.util.List<Quiz> findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(String title, String category);
}
