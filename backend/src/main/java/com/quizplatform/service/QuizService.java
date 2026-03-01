package com.quizplatform.service;

import com.quizplatform.dto.QuizDto;
import java.util.List;

public interface QuizService {
    List<QuizDto> findAll();
    QuizDto createQuiz(com.quizplatform.dto.CreateQuizRequest req);
}
