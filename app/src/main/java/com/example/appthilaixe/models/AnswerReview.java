package com.example.appthilaixe.models;

import java.io.Serializable;

public class AnswerReview implements Serializable {
    private int questionId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String explanation;
    private String imagePath;
    private String userAnswer;
    private boolean isCorrect;

    public AnswerReview(int questionId, String questionText, String optionA, String optionB, String optionC,
                        String optionD, String correctAnswer, String explanation, String imagePath, String userAnswer) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.imagePath = imagePath;
        this.userAnswer = userAnswer;
        this.isCorrect = userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
    }

    public int getQuestionId() {
        return questionId;
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

    public String getImagePath() {
        return imagePath;
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
}
