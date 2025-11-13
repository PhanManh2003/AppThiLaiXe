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

    // 🔹 Lấy danh sách câu hỏi theo testId (JOIN bảng test_question)
    @Query("SELECT q.* FROM questions q " +
            "INNER JOIN test_question tq ON q.questionId = tq.questionId " +
            "WHERE tq.testId = :testId")
    List<Question> getQuestionsByTestId(int testId);

    @Query("DELETE FROM questions")
    void deleteAll();
}
