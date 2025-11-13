package com.example.appthilaixe.models;

import androidx.room.Entity;

@Entity(tableName = "user_test_result", primaryKeys = {"userId", "testId"})
public class UserTestResult {
    public int userId;
    public int testId;
    public double result;

    public UserTestResult() {
    }

    public UserTestResult(int userId, int testId, double result) {
        this.userId = userId;
        this.testId = testId;
        this.result = result;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    
}
