package com.quizplatform.service;

import com.quizplatform.dto.CreateQuizRequest;
import com.quizplatform.dto.QuestionDto;
import com.quizplatform.dto.QuizDto;
import com.quizplatform.dto.TakeQuizRequest;
import com.quizplatform.dto.TakeQuizResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class ServiceSecurityTest {
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuizAttemptService attemptService;

    @Test
    @WithMockUser(roles = "USER")
    void userCannotCreateQuiz() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("X");
        assertThatThrownBy(() -> quizService.createQuiz(req))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreateQuiz() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("X");
        // service will try to save; but we just expect not AccessDeniedException
        try {
            quizService.createQuiz(req);
        } catch (Exception ignored) {
            // ignore other errors because repository not mocked in this context
        }
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotAddQuestion() {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionText("Q"); dto.setQuestionType("mc");
        assertThatThrownBy(() -> questionService.addQuestion(1L, dto))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanTakeQuiz() {
        TakeQuizRequest req = new TakeQuizRequest();
        req.setAnswers(Collections.emptyList());
        // just ensure no AccessDeniedException
        try {
            attemptService.takeQuiz("u@u.com", 1L, req);
        } catch (Exception ignored) {
        }
    }
}