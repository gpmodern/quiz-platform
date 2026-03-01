package com.quizplatform.dto;

import jakarta.validation.constraints.NotNull;

public class AnswerDto {
    @NotNull
    private Long questionId;
    @NotNull
    private String answer;

    public AnswerDto() {}

    public AnswerDto(Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}