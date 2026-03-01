package com.quizplatform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class TakeQuizRequest {
    @NotNull
    private List<AnswerDto> answers;

    public TakeQuizRequest() {}

    public TakeQuizRequest(List<AnswerDto> answers) {
        this.answers = answers;
    }

    public List<AnswerDto> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDto> answers) { this.answers = answers; }
}