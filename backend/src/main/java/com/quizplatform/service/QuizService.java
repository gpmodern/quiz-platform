package com.quizplatform.service;

import com.quizplatform.dto.QuizDto;
import java.util.List;

public interface QuizService {
    List<QuizDto> findAll();
    QuizDto createQuiz(com.quizplatform.dto.CreateQuizRequest req);

    // Sprint 2 additions
    QuizDto findById(Long id);
    QuizDto updateQuiz(Long id, com.quizplatform.dto.CreateQuizRequest req);
    void deleteQuiz(Long id);

    // Sprint 4: search by title/category
    java.util.List<QuizDto> searchQuizzes(String title, String category);
}
