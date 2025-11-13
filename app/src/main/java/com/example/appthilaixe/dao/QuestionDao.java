package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.appthilaixe.models.Question;
import java.util.List;

@Dao
public interface QuestionDao {

    @Insert
    void insertAll(List<Question> questions);

    @Query("SELECT * FROM questions WHERE lessonId = :lessonId")
    List<Question> getByLesson(int lessonId);

    @Query("SELECT * FROM questions")
    List<Question> getAll();

    @Query("DELETE FROM questions")
    void deleteAll();
}
