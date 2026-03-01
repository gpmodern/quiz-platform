package com.quizplatform.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CreateQuizRequest {
    @NotBlank
    private String title;
    private String description;
    private String category;
    private List<QuestionDto> questions;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<QuestionDto> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDto> questions) { this.questions = questions; }
}
