package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.appthilaixe.models.Lesson;
import java.util.List;

@Dao
public interface LessonDao {

    @Insert
    void insertAll(List<Lesson> lessons);

    @Query("SELECT * FROM lessons")
    List<Lesson> getAll();
}
