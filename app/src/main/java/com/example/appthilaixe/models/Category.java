package com.example.appthilaixe.models;

public class Category {
    private int id;
    private String name;
    private String description;
    private int totalQuestions;
    private int completedQuestions;
    private int colorResId;

    public Category(int id, String name, String description, int iconResId, 
                   int totalQuestions, int completedQuestions, int colorResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalQuestions = totalQuestions;
        this.completedQuestions = completedQuestions;
        this.colorResId = colorResId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCompletedQuestions() {
        return completedQuestions;
    }

    public int getColorResId() {
        return colorResId;
    }

    // Calculate progress percentage
    public int getProgressPercentage() {
        if (totalQuestions == 0) return 0;
        return (int) ((completedQuestions * 100.0) / totalQuestions);
    }

    // Setters
    public void setCompletedQuestions(int completedQuestions) {
        this.completedQuestions = completedQuestions;
    }
}

