package com.quizplatform.service;

import com.quizplatform.dto.TakeQuizRequest;
import com.quizplatform.dto.TakeQuizResponse;

public interface QuizAttemptService {
    TakeQuizResponse takeQuiz(String userEmail, Long quizId, TakeQuizRequest request);
}