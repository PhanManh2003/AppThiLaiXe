package com.example.appthilaixe.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tests")
public class Test {
    @PrimaryKey(autoGenerate = true)
    public int testId;

    public String testName;
}
