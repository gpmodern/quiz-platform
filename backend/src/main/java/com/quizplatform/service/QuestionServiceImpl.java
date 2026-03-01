package com.quizplatform.service;

import com.quizplatform.dto.QuestionDto;
import com.quizplatform.entity.Question;
import com.quizplatform.entity.Quiz;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.QuestionRepository;
import com.quizplatform.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<QuestionDto> findByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDto addQuestion(Long quizId, QuestionDto dto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
        Question q = new Question();
        q.setQuiz(quiz);
        q.setQuestionText(dto.getQuestionText());
        q.setQuestionType(dto.getQuestionType());
        q = questionRepository.save(q);
        return toDto(q);
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto dto) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        q.setQuestionText(dto.getQuestionText());
        q.setQuestionType(dto.getQuestionType());
        q = questionRepository.save(q);
        return toDto(q);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    private QuestionDto toDto(Question q) {
        QuestionDto dto = new QuestionDto();
        dto.setId(q.getId());
        dto.setQuizId(q.getQuiz() != null ? q.getQuiz().getId() : null);
        dto.setQuestionText(q.getQuestionText());
        dto.setQuestionType(q.getQuestionType());
        return dto;
    }
}