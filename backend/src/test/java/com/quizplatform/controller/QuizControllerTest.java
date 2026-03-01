package com.quizplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizplatform.dto.CreateQuizRequest;
import com.quizplatform.dto.QuestionDto;
import com.quizplatform.dto.QuizDto;
import com.quizplatform.security.JwtUtil;
import com.quizplatform.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizService quizService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        when(quizService.findAll()).thenReturn(
                Collections.singletonList(new QuizDto(1L, "Title"))
        );
        when(quizService.createQuiz(any(CreateQuizRequest.class))).thenReturn(new QuizDto(2L, "New"));
    }

    @Test
    void list_shouldReturnArray() throws Exception {
        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void create_shouldReturnCreatedQuiz() throws Exception {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("New Quiz Title");
        QuestionDto qdto = new QuestionDto();
        qdto.setQuestionText("Sample question?");
        qdto.setQuestionType("multiple_choice");
        req.setQuestions(Collections.singletonList(qdto));

        mockMvc.perform(post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New"));
    }

    @Test
    void list_withMultipleQuizzes() throws Exception {
        List<QuizDto> quizzes = new ArrayList<>();
        quizzes.add(new QuizDto(1L, "Quiz A"));
        quizzes.add(new QuizDto(2L, "Quiz B"));
        quizzes.add(new QuizDto(3L, "Quiz C"));

        when(quizService.findAll()).thenReturn(quizzes);

        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Quiz A"))
                .andExpect(jsonPath("$[1].title").value("Quiz B"))
                .andExpect(jsonPath("$[2].title").value("Quiz C"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void list_withEmptyList() throws Exception {
        when(quizService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void list_withPaginationParams() throws Exception {
        List<QuizDto> quizzes = Collections.singletonList(new QuizDto(1L, "Paginated"));
        when(quizService.findAll()).thenReturn(quizzes);

        mockMvc.perform(get("/api/quizzes")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Paginated"));
    }

    @Test
    void create_multipleQuestions() throws Exception {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Multi-Question Quiz");
        req.setDescription("A quiz with multiple questions");
        req.setCategory("Science");

        QuestionDto q1 = new QuestionDto();
        q1.setQuestionText("Question 1?");
        q1.setQuestionType("multiple_choice");

        QuestionDto q2 = new QuestionDto();
        q2.setQuestionText("Question 2?");
        q2.setQuestionType("true_false");

        List<QuestionDto> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        req.setQuestions(questions);

        when(quizService.createQuiz(any(CreateQuizRequest.class))).thenReturn(new QuizDto(5L, "Multi-Question Quiz"));

        mockMvc.perform(post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Multi-Question Quiz"));
    }

    @Test
    void create_missingTitle_shouldReturnBadRequest() throws Exception {
        CreateQuizRequest req = new CreateQuizRequest();
        // missing title violates @NotBlank
        QuestionDto qdto = new QuestionDto();
        qdto.setQuestionText("Q?");
        qdto.setQuestionType("multiple_choice");
        req.setQuestions(Collections.singletonList(qdto));

        mockMvc.perform(post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_existing_shouldReturnQuiz() throws Exception {
        QuizDto dto = new QuizDto(10L, "Existing");
        dto.setQuestions(Collections.singletonList(new QuestionDto(1L, 10L, "Q1", "mc")));
        when(quizService.findById(10L)).thenReturn(dto);

        mockMvc.perform(get("/api/quizzes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Existing"))
                .andExpect(jsonPath("$.questions[0].questionText").value("Q1"));
    }

    @Test
    void getById_notFound_shouldReturn404() throws Exception {
        when(quizService.findById(11L)).thenThrow(new com.quizplatform.exception.ResourceNotFoundException("not"));

        mockMvc.perform(get("/api/quizzes/11"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_success() throws Exception {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Updated Title");

        when(quizService.updateQuiz(any(Long.class), any(CreateQuizRequest.class)))
                .thenReturn(new QuizDto(20L, "Updated Title"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/quizzes/20")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void update_notFound_shouldReturn404() throws Exception {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Whatever");
        when(quizService.updateQuiz(any(Long.class), any(CreateQuizRequest.class)))
                .thenThrow(new com.quizplatform.exception.ResourceNotFoundException("not"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/quizzes/21")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_success() throws Exception {
        // no exception thrown
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/quizzes/30"))
                .andExpect(status().isNoContent());
    }
}
