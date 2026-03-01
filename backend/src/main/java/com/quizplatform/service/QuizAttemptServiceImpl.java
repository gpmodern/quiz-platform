package com.quizplatform.service;

import com.quizplatform.dto.AnswerDto;
import com.quizplatform.dto.TakeQuizRequest;
import com.quizplatform.dto.TakeQuizResponse;
import com.quizplatform.entity.Quiz;
import com.quizplatform.entity.QuizAttempt;
import com.quizplatform.entity.Question;
import com.quizplatform.entity.User;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.QuizAttemptRepository;
import com.quizplatform.repository.QuizRepository;
import com.quizplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('USER','ADMIN')")
    public TakeQuizResponse takeQuiz(String userEmail, Long quizId, TakeQuizRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        // map questions to correct answers
        Map<Long, String> correctMap = quiz.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Question::getCorrectAnswer));

        int total = correctMap.size();
        int score = 0;
        for (AnswerDto ans : request.getAnswers()) {
            String correct = correctMap.get(ans.getQuestionId());
            if (correct != null && correct.equalsIgnoreCase(ans.getAnswer())) {
                score++;
            }
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setScore(score);
        attempt.setTotal(total);
        attemptRepository.save(attempt);

        return new TakeQuizResponse(score, total);
    }
}