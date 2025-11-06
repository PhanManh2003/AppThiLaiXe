package com.example.appthilaixe.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exam_results")
public class ExamResultEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int score;
    private int totalQuestions;
    private String category;
    private String dateTaken;

    public ExamResultEntity(int userId, int score, int totalQuestions, String category, String dateTaken) {
        this.userId = userId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.category = category;
        this.dateTaken = dateTaken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }
}
