package com.example.appthilaixe.models;

import java.io.Serializable;

public class ExamResult implements Serializable {
    private int id;
    private String categoryName;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int score;
    private String dateTaken;

    public ExamResult(int id, String categoryName, int totalQuestions, int correctAnswers,
                      int wrongAnswers, int score, String dateTaken) {
        this.id = id;
        this.categoryName = categoryName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.score = score;
        this.dateTaken = dateTaken;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public int getScore() {
        return score;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public int getCorrectPercent() {
        if (totalQuestions == 0) return 0;
        return (int) ((correctAnswers * 100.0) / totalQuestions);
    }
}
