package com.quizplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizplatform.dto.QuestionDto;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private QuestionService questionService;

    @MockBean
    private com.quizplatform.security.JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        // default stubs
        QuestionDto sample = new QuestionDto(1L, 2L, "Q", "type");
        when(questionService.findByQuizId(2L)).thenReturn(Collections.singletonList(sample));
        when(questionService.addQuestion(any(Long.class), any(QuestionDto.class))).thenReturn(sample);
        when(questionService.updateQuestion(any(Long.class), any(QuestionDto.class))).thenReturn(sample);
        doNothing().when(questionService).deleteQuestion(any(Long.class));
    }

    @Test
    void listByQuiz_shouldReturnQuestions() throws Exception {
        mockMvc.perform(get("/api/quizzes/2/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText").value("Q"));
    }

    @Test
    void addQuestion_shouldReturnCreated() throws Exception {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionText("Q1");
        dto.setQuestionType("mc");

        mockMvc.perform(post("/api/quizzes/2/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateQuestion_shouldReturnUpdated() throws Exception {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionText("Updated");
        dto.setQuestionType("tf");

        mockMvc.perform(put("/api/questions/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText").value("Q")); // service returns sample
    }

    @Test
    void updateQuestion_notFound_returns404() throws Exception {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionText("X");
        dto.setQuestionType("Y");
        when(questionService.updateQuestion(any(Long.class), any(QuestionDto.class)))
                .thenThrow(new ResourceNotFoundException("no"));

        mockMvc.perform(put("/api/questions/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteQuestion_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/questions/10"))
                .andExpect(status().isNoContent());
    }
}