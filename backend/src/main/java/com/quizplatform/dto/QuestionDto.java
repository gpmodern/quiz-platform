package com.quizplatform.dto;

import jakarta.validation.constraints.NotBlank;

public class QuestionDto {
    @NotBlank
    private String questionText;
    @NotBlank
    private String questionType;

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
}
