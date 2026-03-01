package com.quizplatform.service;

import com.quizplatform.dto.AnswerDto;
import com.quizplatform.dto.TakeQuizRequest;
import com.quizplatform.dto.TakeQuizResponse;
import com.quizplatform.entity.Question;
import com.quizplatform.entity.Quiz;
import com.quizplatform.entity.User;
import com.quizplatform.repository.QuizAttemptRepository;
import com.quizplatform.repository.QuizRepository;
import com.quizplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizAttemptServiceImplTest {
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private QuizAttemptRepository attemptRepository;
    @InjectMocks
    private QuizAttemptServiceImpl attemptService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void takeQuiz_correctAnswers_shouldScoreAll() {
        User u = new User(); u.setId(1L); u.setEmail("u@u.com");
        when(userRepository.findByEmail("u@u.com")).thenReturn(Optional.of(u));
        Quiz quiz = new Quiz(); quiz.setId(2L);
        Question q1 = new Question(); q1.setId(10L); q1.setCorrectAnswer("yes");
        Question q2 = new Question(); q2.setId(11L); q2.setCorrectAnswer("no");
        quiz.setQuestions(Arrays.asList(q1, q2));
        when(quizRepository.findById(2L)).thenReturn(Optional.of(quiz));
        when(attemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TakeQuizRequest req = new TakeQuizRequest();
        req.setAnswers(Arrays.asList(
                new AnswerDto(10L, "yes"),
                new AnswerDto(11L, "no")
        ));

        TakeQuizResponse resp = attemptService.takeQuiz("u@u.com", 2L, req);
        assertThat(resp.getScore()).isEqualTo(2);
        assertThat(resp.getTotal()).isEqualTo(2);
    }

    @Test
    void takeQuiz_partialScore() {
        User u = new User(); u.setEmail("u@u.com");
        when(userRepository.findByEmail("u@u.com")).thenReturn(Optional.of(u));
        Quiz quiz = new Quiz(); quiz.setId(2L);
        Question q1 = new Question(); q1.setId(10L); q1.setCorrectAnswer("yes");
        quiz.setQuestions(Arrays.asList(q1));
        when(quizRepository.findById(2L)).thenReturn(Optional.of(quiz));
        when(attemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TakeQuizRequest req = new TakeQuizRequest();
        req.setAnswers(Arrays.asList(new AnswerDto(10L, "no")));

        TakeQuizResponse resp = attemptService.takeQuiz("u@u.com", 2L, req);
        assertThat(resp.getScore()).isEqualTo(0);
        assertThat(resp.getTotal()).isEqualTo(1);
    }
}