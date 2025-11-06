package com.example.appthilaixe.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "user_answers",
        foreignKeys = @ForeignKey(
                entity = ExamResultEntity.class,
                parentColumns = "id",
                childColumns = "examResultId",
                onDelete = ForeignKey.CASCADE
        )
)
public class UserAnswerEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int examResultId;
    private int questionId;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public UserAnswerEntity(int examResultId, int questionId, String selectedAnswer,
                            String correctAnswer, boolean isCorrect) {
        this.examResultId = examResultId;
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(int examResultId) {
        this.examResultId = examResultId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
