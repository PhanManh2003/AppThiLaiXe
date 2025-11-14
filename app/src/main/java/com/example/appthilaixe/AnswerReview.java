package com.example.appthilaixe;

import java.io.Serializable;

/**
 * Model dùng để xem lại đáp án
 */
public class AnswerReview implements Serializable {

    private int questionId;
    private String questionText;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean answered;   // đã chọn đáp án hay chưa
    private boolean correct;    // đúng hay sai

    public AnswerReview() {
    }

    public AnswerReview(int questionId,
                        String questionText,
                        String selectedAnswer,
                        String correctAnswer,
                        boolean answered,
                        boolean correct) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.answered = answered;
        this.correct = correct;
    }

    // ====== GETTER / SETTER cần thiết ======

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
