package com.example.appthilaixe.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(QuestionEntity question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionEntity> questions);

    @Query("SELECT * FROM questions")
    List<QuestionEntity> getAllQuestions();

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    List<QuestionEntity> getByCategory(int categoryId);

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    QuestionEntity getById(int id);

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 30")
    List<QuestionEntity> getRandom30();

    @Query("DELETE FROM questions")
    void clear();
}
