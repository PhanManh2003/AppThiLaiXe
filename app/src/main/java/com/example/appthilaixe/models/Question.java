package com.example.appthilaixe.models;

import java.io.Serializable;

public class Question implements Serializable {
    private int id;
    private int categoryId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer; // "A", "B", "C", or "D"
    private String explanation;
    private String imageResId; // Optional image resource ID

    public Question(int id, int categoryId, String questionText, String optionA, 
                   String optionB, String optionC, String optionD, 
                   String correctAnswer, String explanation, String imageResId) {
        this.id = id;
        this.categoryId = categoryId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.imageResId = imageResId;
    }

    // Constructor without image
    public Question(int id, int categoryId, String questionText, String optionA, 
                   String optionB, String optionC, String optionD, 
                   String correctAnswer, String explanation) {
        this(id, categoryId, questionText, optionA, optionB, optionC, optionD, 
             correctAnswer, explanation, null);
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getImageResId() {
        return imageResId;
    }

    public boolean hasImage() {
        return imageResId != null;
    }

    // Check if a given answer is correct
    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equals(answer);
    }
}

