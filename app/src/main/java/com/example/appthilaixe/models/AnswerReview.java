package com.example.appthilaixe.models;

import java.io.Serializable;

public class AnswerReview implements Serializable {
    private Question question;
    private String userAnswer; // "A", "B", "C", "D", or null if not answered
    private boolean isCorrect;
    
    public AnswerReview(Question question, String userAnswer) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.isCorrect = (userAnswer != null && question.isCorrectAnswer(userAnswer));
    }
    
    public Question getQuestion() {
        return question;
    }
    
    public String getUserAnswer() {
        return userAnswer;
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    public boolean isAnswered() {
        return userAnswer != null && !userAnswer.isEmpty();
    }
    
    public String getCorrectAnswer() {
        return question.getCorrectAnswer();
    }
}

