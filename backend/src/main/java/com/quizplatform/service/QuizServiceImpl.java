package com.quizplatform.service;

import com.quizplatform.dto.QuizDto;
import com.quizplatform.repository.QuizRepository;
import com.quizplatform.repository.QuestionRepository;
import com.quizplatform.dto.CreateQuizRequest;
import com.quizplatform.dto.QuestionDto;
import com.quizplatform.entity.Quiz;
import com.quizplatform.entity.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.quizplatform.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository repository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<QuizDto> findAll() {
        return repository.findAll().stream()
                .map(q -> new QuizDto(q.getId(), q.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public QuizDto createQuiz(CreateQuizRequest req) {
        Quiz quiz = new Quiz();
        quiz.setTitle(req.getTitle());
        quiz.setDescription(req.getDescription());
        quiz.setCategory(req.getCategory());
        quiz = repository.save(quiz);

        if (req.getQuestions() != null) {
            for (QuestionDto qd : req.getQuestions()) {
                Question q = new Question();
                q.setQuiz(quiz);
                q.setQuestionText(qd.getQuestionText());
                q.setQuestionType(qd.getQuestionType());
                q.setCorrectAnswer(qd.getCorrectAnswer());
                questionRepository.save(q);
            }
        }

        return new QuizDto(quiz.getId(), quiz.getTitle());
    }

    // Sprint 2 implementations

    @Override
    public QuizDto findById(Long id) {
        Quiz q = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
        // map questions if present
        java.util.List<QuestionDto> questions = q.getQuestions().stream()
                .map(qq -> new QuestionDto(qq.getId(), q.getId(), qq.getQuestionText(), qq.getQuestionType(), qq.getCorrectAnswer()))
                .collect(Collectors.toList());
        return new QuizDto(q.getId(), q.getTitle(), questions);
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public QuizDto updateQuiz(Long id, CreateQuizRequest req) {
        Quiz quiz = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
        quiz.setTitle(req.getTitle());
        quiz.setDescription(req.getDescription());
        quiz.setCategory(req.getCategory());
        quiz = repository.save(quiz);
        return new QuizDto(quiz.getId(), quiz.getTitle());
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public void deleteQuiz(Long id) {
        repository.deleteById(id);
    }

    // Sprint 4 search implementation
    @Override
    @org.springframework.security.access.prepost.PreAuthorize("permitAll()")
    public java.util.List<QuizDto> searchQuizzes(String title, String category) {
        java.util.List<Quiz> results;
        if (title != null && !title.isEmpty() && category != null && !category.isEmpty()) {
            results = repository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(title, category);
        } else if (title != null && !title.isEmpty()) {
            results = repository.findByTitleContainingIgnoreCase(title);
        } else if (category != null && !category.isEmpty()) {
            results = repository.findByCategoryIgnoreCase(category);
        } else {
            results = repository.findAll();
        }
        return results.stream()
                .map(q -> new QuizDto(q.getId(), q.getTitle()))
                .collect(Collectors.toList());
    }
}
