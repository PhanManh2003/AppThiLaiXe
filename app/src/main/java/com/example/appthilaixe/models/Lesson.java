package com.example.appthilaixe.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lessons")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    public int lessonId;
    public String lessonName;
}
