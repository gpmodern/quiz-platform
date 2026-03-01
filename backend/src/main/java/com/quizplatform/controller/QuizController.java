package com.quizplatform.controller;

import com.quizplatform.dto.CreateQuizRequest;
import com.quizplatform.dto.QuizDto;
import com.quizplatform.service.QuizService;
// import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@Validated
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<QuizDto> list = quizService.findAll(); // simple for MVP; pagination can be added in service
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuizDto>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category) {
        List<QuizDto> results = quizService.searchQuizzes(title, category);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizDto> create(@Valid @RequestBody CreateQuizRequest req) {
        QuizDto dto = quizService.createQuiz(req);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getById(@PathVariable Long id) {
        QuizDto dto = quizService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizDto> update(@PathVariable Long id, @Valid @RequestBody CreateQuizRequest req) {
        QuizDto dto = quizService.updateQuiz(id, req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }
}

