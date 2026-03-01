package com.quizplatform.controller;

import com.quizplatform.dto.QuestionDto;
import com.quizplatform.dto.TakeQuizRequest;
import com.quizplatform.dto.TakeQuizResponse;
import com.quizplatform.service.QuizAttemptService;
import com.quizplatform.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
@Validated
public class QuizAttemptController {
    private final QuizAttemptService attemptService;
    private final QuizService quizService;

    public QuizAttemptController(QuizAttemptService attemptService, QuizService quizService) {
        this.attemptService = attemptService;
        this.quizService = quizService;
    }

    // helper to grab current user's email from security context
    private String currentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    @GetMapping("/{id}/take")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<QuestionDto>> getQuestionsForQuiz(@PathVariable Long id) {
        // return questions without correct answers
        List<QuestionDto> quiz = quizService.findById(id).getQuestions();
        // strip correctAnswer from the DTO
        List<QuestionDto> result = quiz.stream().map(q -> {
            QuestionDto copy = new QuestionDto();
            copy.setId(q.getId());
            copy.setQuizId(q.getQuizId());
            copy.setQuestionText(q.getQuestionText());
            copy.setQuestionType(q.getQuestionType());
            return copy;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/take")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<TakeQuizResponse> submitAnswers(@PathVariable Long id,
                                                           @Valid @RequestBody TakeQuizRequest request) {
        String email = currentUserEmail();
        TakeQuizResponse resp = attemptService.takeQuiz(email, id, request);
        return ResponseEntity.ok(resp);
    }
}