package com.example.appthilaixe.models;

import androidx.room.Entity;

@Entity(tableName = "user_test_result", primaryKeys = {"userId", "testId"})
public class UserTestResult {
    public int userId;
    public int testId;
    public double result;
}
