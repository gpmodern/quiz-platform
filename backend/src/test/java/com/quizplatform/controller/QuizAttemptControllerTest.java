package com.quizplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizplatform.dto.AnswerDto;
import com.quizplatform.dto.TakeQuizRequest;
import com.quizplatform.dto.TakeQuizResponse;
import com.quizplatform.service.QuizAttemptService;
import com.quizplatform.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizAttemptController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizAttemptControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizAttemptService attemptService;
    @MockBean
    private QuizService quizService;
    @MockBean
    private com.quizplatform.security.JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        when(attemptService.takeQuiz(eq("user@u.com"), any(Long.class), any(TakeQuizRequest.class)))
                .thenReturn(new TakeQuizResponse(1, 2));
        // quizService.findById return empty quiz with no questions
        when(quizService.findById(any(Long.class))).thenReturn(new com.quizplatform.dto.QuizDto());
    }

    @Test
    void getQuestions_returnsEmpty() throws Exception {
        mockMvc.perform(get("/api/quizzes/5/take"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void submitAnswers_returnsScore() throws Exception {
        // set a dummy authentication so currentUserEmail() won't NPE
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@u.com", null, Collections.emptyList()));
        TakeQuizRequest req = new TakeQuizRequest();
        req.setAnswers(Collections.singletonList(new AnswerDto(1L, "yes")));
        mockMvc.perform(post("/api/quizzes/5/take")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(1));
    }
}