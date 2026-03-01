package com.quizplatform.controller;

import com.quizplatform.dto.QuestionDto;
import com.quizplatform.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/quizzes/{quizId}/questions")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionDto>> listByQuiz(@PathVariable Long quizId) {
        List<QuestionDto> list = questionService.findByQuizId(quizId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionDto> add(@PathVariable Long quizId, @Valid @RequestBody QuestionDto dto) {
        QuestionDto result = questionService.addQuestion(quizId, dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/questions/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionDto> update(@PathVariable Long id, @Valid @RequestBody QuestionDto dto) {
        QuestionDto result = questionService.updateQuestion(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/questions/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}