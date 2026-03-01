package com.quizplatform.dto;

public class QuizDto {
    private Long id;
    private String title;
    private java.util.List<QuestionDto> questions = new java.util.ArrayList<>();

    public QuizDto() {}

    public QuizDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public QuizDto(Long id, String title, java.util.List<QuestionDto> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public java.util.List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(java.util.List<QuestionDto> questions) {
        this.questions = questions;
    }
}
