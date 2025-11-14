package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appthilaixe.models.UserTestResult;

import java.util.List;

@Dao
public interface UserTestResultDao {

    // Lưu 1 lần thi
    @Insert
    void insert(UserTestResult result);

    // Seed dữ liệu từ file JSON
    @Insert
    void insertAll(List<UserTestResult> results);

    // Lấy lịch sử theo user
    @Query("SELECT * FROM user_test_result WHERE userId = :userId ORDER BY testId DESC")
    List<UserTestResult> getByUserId(int userId);

    @Query("SELECT * FROM user_test_result")
    List<UserTestResult> getAll();

    @Query("DELETE FROM user_test_result")
    void deleteAll();
}
