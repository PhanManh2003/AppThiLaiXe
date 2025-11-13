package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appthilaixe.models.TestQuestionCrossRef;

import java.util.List;
@Dao
public interface TestQuestionDao {
    // Thêm danh sách liên kết Test - Question
    @Insert
    void insertAll(List<TestQuestionCrossRef> refs);

    // Lấy toàn bộ bản ghi
    @Query("SELECT * FROM test_question")
    List<TestQuestionCrossRef> getAll();

    // Lấy danh sách QuestionId theo TestId
    @Query("SELECT questionId FROM test_question WHERE testId = :testId")
    List<Integer> getQuestionIdsForTest(int testId);

    @Query("DELETE FROM test_question")
    void deleteAll();
}
