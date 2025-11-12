package com.example.appthilaixe.models;

import androidx.room.Entity;

@Entity(tableName = "study", primaryKeys = {"userId", "lessonId"})
public class Study {
    public int userId;
    public int lessonId;
    public int learnedCount;
}
