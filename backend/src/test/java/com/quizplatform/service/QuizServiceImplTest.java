package com.quizplatform.service;

import com.quizplatform.dto.CreateQuizRequest;
import com.quizplatform.dto.QuestionDto;
import com.quizplatform.dto.QuizDto;
import com.quizplatform.entity.Question;
import com.quizplatform.entity.Quiz;
import com.quizplatform.repository.QuestionRepository;
import com.quizplatform.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnDtoList() {
        Quiz q1 = new Quiz();
        q1.setId(1L);
        q1.setTitle("A");
        Quiz q2 = new Quiz();
        q2.setId(2L);
        q2.setTitle("B");
        when(quizRepository.findAll()).thenReturn(Arrays.asList(q1, q2));

        List<QuizDto> result = quizService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("A");
        assertThat(result.get(1).getTitle()).isEqualTo("B");
        verify(quizRepository, times(1)).findAll();
    }

    @Test
    void createQuiz_shouldPersistQuizAndQuestions() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Quiz 1");
        req.setDescription("Desc");
        req.setCategory("Cat");
        QuestionDto qdto = new QuestionDto();
        qdto.setQuestionText("Q?");
        qdto.setQuestionType("multiple_choice");
        req.setQuestions(Collections.singletonList(qdto));

        Quiz saved = new Quiz();
        saved.setId(10L);
        saved.setTitle("Quiz 1");
        when(quizRepository.save(any())).thenReturn(saved);

        QuizDto dto = quizService.createQuiz(req);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getTitle()).isEqualTo("Quiz 1");

        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getQuestionText()).isEqualTo("Q?");
        assertThat(captor.getValue().getQuestionType()).isEqualTo("multiple_choice");
    }

    @Test
    void createQuiz_repositoryThrows_shouldPropagate() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Quiz Error");

        when(quizRepository.save(any())).thenThrow(new RuntimeException("DB down"));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            quizService.createQuiz(req);
        });
    }

    @Test
    void createQuiz_withNullQuestions_shouldNotCallQuestionRepo() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("No Questions Quiz");

        Quiz saved = new Quiz();
        saved.setId(11L);
        saved.setTitle("No Questions Quiz");
        when(quizRepository.save(any())).thenReturn(saved);

        QuizDto dto = quizService.createQuiz(req);

        assertThat(dto.getId()).isEqualTo(11L);
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void createQuiz_questionSaveThrows_shouldPropagate() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Quiz With Bad Question");
        QuestionDto qdto = new QuestionDto();
        qdto.setQuestionText("Q?");
        qdto.setQuestionType("multiple_choice");
        req.setQuestions(Collections.singletonList(qdto));

        Quiz saved = new Quiz();
        saved.setId(12L);
        saved.setTitle("Quiz With Bad Question");
        when(quizRepository.save(any())).thenReturn(saved);
        doThrow(new RuntimeException("question save failed")).when(questionRepository).save(any(Question.class));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> quizService.createQuiz(req));
    }

    @Test
    void findAll_emptyShouldReturnEmptyList() {
        when(quizRepository.findAll()).thenReturn(Collections.emptyList());

        List<QuizDto> result = quizService.findAll();

        assertThat(result).isEmpty();
        verify(quizRepository, times(1)).findAll();
    }

    // sprint 2 tests
    @Test
    void findById_shouldReturnDto() {
        Quiz quiz = new Quiz();
        quiz.setId(5L);
        quiz.setTitle("Sample");
        // quiz has no questions
        when(quizRepository.findById(5L)).thenReturn(java.util.Optional.of(quiz));

        QuizDto dto = quizService.findById(5L);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getTitle()).isEqualTo("Sample");
        assertThat(dto.getQuestions()).isEmpty();
    }

    @Test
    void findById_notFound_shouldThrow() {
        when(quizRepository.findById(99L)).thenReturn(java.util.Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(com.quizplatform.exception.ResourceNotFoundException.class, () -> quizService.findById(99L));
    }

    @Test
    void updateQuiz_success() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Updated");
        req.setDescription("Desc");
        req.setCategory("Cat");
        Quiz existing = new Quiz();
        existing.setId(7L);
        existing.setTitle("Old");
        when(quizRepository.findById(7L)).thenReturn(java.util.Optional.of(existing));
        when(quizRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        QuizDto result = quizService.updateQuiz(7L, req);
        assertThat(result.getTitle()).isEqualTo("Updated");
        verify(quizRepository).save(existing);
    }

    @Test
    void updateQuiz_notFound_shouldThrow() {
        when(quizRepository.findById(8L)).thenReturn(java.util.Optional.empty());
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Whatever");
        org.junit.jupiter.api.Assertions.assertThrows(com.quizplatform.exception.ResourceNotFoundException.class, () -> quizService.updateQuiz(8L, req));
    }

    @Test
    void deleteQuiz_shouldCallRepository() {
        quizService.deleteQuiz(42L);
        verify(quizRepository).deleteById(42L);
    }

    // sprint4 search tests
    @Test
    void search_withTitle_shouldReturnMatches() {
        Quiz q = new Quiz();
        q.setId(1L);
        q.setTitle("FindMe");
        when(quizRepository.findByTitleContainingIgnoreCase("find")).thenReturn(java.util.Collections.singletonList(q));

        java.util.List<QuizDto> result = quizService.searchQuizzes("find", null);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("FindMe");
    }

    @Test
    void search_withCategory_shouldReturnMatches() {
        Quiz q = new Quiz();
        q.setId(2L);
        q.setTitle("Other");
        when(quizRepository.findByCategoryIgnoreCase("math")).thenReturn(java.util.Collections.singletonList(q));

        java.util.List<QuizDto> result = quizService.searchQuizzes(null, "math");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void search_withTitleAndCategory_shouldReturnMatches() {
        Quiz q = new Quiz();
        q.setId(3L);
        q.setTitle("Combo");
        when(quizRepository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase("co", "science"))
                .thenReturn(java.util.Collections.singletonList(q));

        java.util.List<QuizDto> result = quizService.searchQuizzes("co", "science");
        assertThat(result).hasSize(1);
    }

    @Test
    void search_withNoParams_shouldReturnAll() {
        Quiz q1 = new Quiz(); q1.setId(5L); q1.setTitle("A");
        when(quizRepository.findAll()).thenReturn(java.util.Arrays.asList(q1));

        java.util.List<QuizDto> result = quizService.searchQuizzes(null, null);
        assertThat(result).hasSize(1);
    }

    // question service behaviours exercised indirectly through findById returned questions
    // additional direct tests implemented in QuestionServiceImplTest
}
