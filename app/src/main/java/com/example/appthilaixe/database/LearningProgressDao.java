package com.example.appthilaixe.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LearningProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(LearningProgressEntity progress);

    @Update
    void update(LearningProgressEntity progress);

    @Query("SELECT * FROM learning_progress WHERE userId = :userId AND categoryId = :categoryId LIMIT 1")
    LearningProgressEntity getOne(int userId, int categoryId);

    @Query("SELECT * FROM learning_progress WHERE userId = :userId")
    List<LearningProgressEntity> getAllByUser(int userId);

    @Query("UPDATE learning_progress SET learnedCount = :learned, lastQuestionIndex = :lastIndex, lastTime = :time WHERE userId = :userId AND categoryId = :categoryId")
    void updateCounts(int userId, int categoryId, int learned, int lastIndex, long time);
}
