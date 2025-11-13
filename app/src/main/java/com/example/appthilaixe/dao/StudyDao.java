package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appthilaixe.models.Study;

import java.util.List;

@Dao
public interface StudyDao {

    @Query("SELECT * FROM study")
    List<Study> getAll();

    @Query("SELECT * FROM study WHERE userId = :userId AND lessonId = :lessonId LIMIT 1")
    Study getStudy(int userId, int lessonId);

    @Insert
    void insert(Study study);

    @Update
    void update(Study study);

    // cập nhật tiến độ học
    @Query("UPDATE study SET learnedCount = :count WHERE userId = :userId AND lessonId = :lessonId")
    void updateLearned(int userId, int lessonId, int count);

    @Query("DELETE FROM study")
    void deleteAll();
}
