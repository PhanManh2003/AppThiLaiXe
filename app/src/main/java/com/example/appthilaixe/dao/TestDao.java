package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appthilaixe.models.Test;

import java.util.List;
@Dao
public interface TestDao {
    // Thêm nhiều Test
    @Insert
    void insertAll(List<Test> tests);

    // Lấy tất cả Test
    @Query("SELECT * FROM tests")
    List<Test> getAll();

    // Lấy 1 Test theo ID
    @Query("SELECT * FROM tests WHERE testId = :id LIMIT 1")
    Test getById(int id);

    // xoá tất
    @Query("DELETE FROM tests")
    void deleteAll();
}
