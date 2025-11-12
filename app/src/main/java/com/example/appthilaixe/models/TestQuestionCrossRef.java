package com.example.appthilaixe.models;

import androidx.room.Entity;

@Entity(tableName = "test_question", primaryKeys = {"testId", "questionId"})
public class TestQuestionCrossRef {
    public int testId;
    public int questionId;
}
