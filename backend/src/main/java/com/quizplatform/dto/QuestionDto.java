package com.quizplatform.dto;

import jakarta.validation.constraints.NotBlank;

public class QuestionDto {
    private Long id;
    private Long quizId;

    @NotBlank
    private String questionText;
    @NotBlank
    private String questionType;
    private String correctAnswer;

    public QuestionDto() {}

    public QuestionDto(Long id, Long quizId, String questionText, String questionType) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
        this.questionType = questionType;
    }

    public QuestionDto(Long id, Long quizId, String questionText, String questionType, String correctAnswer) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.correctAnswer = correctAnswer;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
