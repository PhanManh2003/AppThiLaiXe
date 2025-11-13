package com.example.appthilaixe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appthilaixe.models.*;
import com.example.appthilaixe.dao.*;

@Database(
        entities = {
                User.class,
                Lesson.class,
                Question.class,
                Study.class,
                Test.class,
                TestQuestionCrossRef.class,
                UserTestResult.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * INSTANCE là biến lưu trữ duy nhất (singleton) của database trong toàn bộ app.
     * Từ khóa volatile đảm bảo:
     * - Mọi thread đều thấy cùng một bản cập nhật mới nhất của INSTANCE.
     * - Tránh tình trạng tạo nhiều DB khi nhiều luồng cùng truy cập.
     */
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract LessonDao lessonDao();

    public abstract QuestionDao questionDao();

    public abstract StudyDao studyDao();

    public abstract TestDao testDao();

    public abstract TestQuestionDao testQuestionDao();

    /**
     * Hàm getInstance() trả về instance duy nhất của database.
     * Sử dụng double-checked locking để đảm bảo an toàn luồng.
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "appthilaixe.db"
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
