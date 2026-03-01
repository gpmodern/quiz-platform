package com.quizplatform.service;

import com.quizplatform.dto.QuestionDto;
import com.quizplatform.entity.Question;
import com.quizplatform.entity.Quiz;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.QuestionRepository;
import com.quizplatform.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuizRepository quizRepository;
    @InjectMocks
    private QuestionServiceImpl questionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByQuizId_shouldReturnList() {
        Question q = new Question();
        q.setId(1L);
        q.setQuestionText("Q1");
        q.setQuestionType("mc");
        when(questionRepository.findByQuizId(9L)).thenReturn(Collections.singletonList(q));

        java.util.List<QuestionDto> list = questionService.findByQuizId(9L);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getQuestionText()).isEqualTo("Q1");
    }

    @Test
    void addQuestion_quizNotFound_shouldThrow() {
        when(quizRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> questionService.addQuestion(100L, new QuestionDto()));
    }

    @Test
    void addQuestion_success() {
        Quiz quiz = new Quiz();
        quiz.setId(2L);
        when(quizRepository.findById(2L)).thenReturn(Optional.of(quiz));
        Question saved = new Question();
        saved.setId(3L);
        saved.setQuiz(quiz);
        saved.setQuestionText("Txt");
        saved.setQuestionType("type");
        when(questionRepository.save(any())).thenReturn(saved);

        QuestionDto dto = new QuestionDto();
        dto.setQuestionText("Txt");
        dto.setQuestionType("type");
        QuestionDto result = questionService.addQuestion(2L, dto);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getQuizId()).isEqualTo(2L);
    }

    @Test
    void updateQuestion_notFound_shouldThrow() {
        when(questionRepository.findById(55L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> questionService.updateQuestion(55L, new QuestionDto()));
    }

    @Test
    void updateQuestion_success() {
        Question q = new Question();
        q.setId(10L);
        q.setQuestionText("Old");
        when(questionRepository.findById(10L)).thenReturn(Optional.of(q));
        when(questionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        QuestionDto dto = new QuestionDto();
        dto.setQuestionText("New");
        dto.setQuestionType("type");
        QuestionDto result = questionService.updateQuestion(10L, dto);
        assertThat(result.getQuestionText()).isEqualTo("New");
    }

    @Test
    void deleteQuestion_shouldCallRepo() {
        questionService.deleteQuestion(77L);
        verify(questionRepository).deleteById(77L);
    }
}