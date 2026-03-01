package com.quizplatform.service;

import com.quizplatform.dto.QuestionDto;
import java.util.List;

public interface QuestionService {
    List<QuestionDto> findByQuizId(Long quizId);
    QuestionDto addQuestion(Long quizId, QuestionDto dto);
    QuestionDto updateQuestion(Long id, QuestionDto dto);
    void deleteQuestion(Long id);
}