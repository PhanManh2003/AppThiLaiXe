package com.example.appthilaixe.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "learning_progress")
public class LearningProgressEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public int categoryId;

    public int learnedCount;
    public int lastQuestionIndex;
    public long lastTime;

    public LearningProgressEntity(int userId, int categoryId, int learnedCount, int lastQuestionIndex, long lastTime) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.learnedCount = learnedCount;
        this.lastQuestionIndex = lastQuestionIndex;
        this.lastTime = lastTime;
    }
}
