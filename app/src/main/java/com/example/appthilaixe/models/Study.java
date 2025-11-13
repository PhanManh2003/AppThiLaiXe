package com.example.appthilaixe.models;

import androidx.room.Entity;

@Entity(tableName = "study", primaryKeys = {"userId", "lessonId"})
public class Study {
    public int userId;
    public int lessonId;
    public int learnedCount;

    public Study() {
    }

    public Study(int userId, int lessonId, int learnedCount) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.learnedCount = learnedCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getLearnedCount() {
        return learnedCount;
    }

    public void setLearnedCount(int learnedCount) {
        this.learnedCount = learnedCount;
    }

    @Override
    public String toString() {
        return "Study{" +
                "userId=" + userId +
                ", lessonId=" + lessonId +
                ", learnedCount=" + learnedCount +
                '}';
    }
}
