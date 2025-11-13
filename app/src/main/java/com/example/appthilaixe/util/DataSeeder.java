package com.example.appthilaixe.util;

import android.content.Context;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.*;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DataSeeder {

    public static class SeedData {
        @SerializedName("users")
        public List<User> users;
        @SerializedName("lessons")
        public List<Lesson> lessons;
        @SerializedName("questions")
        public List<Question> questions;
        @SerializedName("tests")
        public List<Test> tests;
        @SerializedName("test_question")
        public List<TestQuestionCrossRef> testQuestionRefs;
    }

    // File: DataSeeder.java
    public static void seedAll(Context context, AppDatabase db) {
        // 🔹 BẮT BUỘC: Chạy toàn bộ logic seed trên luồng nền
        new Thread(() -> {
            try {
                String json = JsonUtils.loadJSON(context, "seed_data.json");
                if (json == null || json.isEmpty()) {
                    System.out.println(" Không tìm thấy file seed_data.json trong assets/");
                    return;
                }

                SeedData data = new Gson().fromJson(json, SeedData.class);
                if (data == null) {
                    System.out.println("⚠Không thể parse JSON thành đối tượng SeedData");
                    return;
                }

                // ---- USERS ----
                if (data.users != null && db.userDao().getAllUsers().isEmpty()) {
                    db.userDao().insertAll(data.users);
                    System.out.println(" Đã seed " + data.users.size() + " users");
                }

                // ---- LESSONS ----
                if (data.lessons != null && db.lessonDao().getAll().isEmpty()) {
                    db.lessonDao().insertAll(data.lessons);
                    System.out.println(" Đã seed " + data.lessons.size() + " lessons");
                }

                // ---- QUESTIONS ----
                if (data.questions != null && db.questionDao().getAll().isEmpty()) {
                    db.questionDao().insertAll(data.questions);
                    System.out.println(" Đã seed " + data.questions.size() + " questions");
                }
                // ---- TESTS ----
                if (data.tests != null && db.testDao().getAll().isEmpty()) {
                    db.testDao().insertAll(data.tests);
                    System.out.println("Đã Seed " + data.tests.size() + " tests");
                }

                // ---- TEST_QUESTION ----
                if (data.testQuestionRefs != null && db.testQuestionDao().getAll().isEmpty()) {
                    db.testQuestionDao().insertAll(data.testQuestionRefs);
                    System.out.println("Đã Seed " + data.testQuestionRefs.size() + " test-question refs");
                }
                System.out.println(" Seed dữ liệu hoàn tất!");
            } catch (Exception e) {
                System.err.println(" Lỗi khi seed database: " + e.getMessage());
                e.printStackTrace();
            }
        }).start(); // <--- .start() để chạy luồng
    }
}
