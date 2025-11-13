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
     * Biến INSTANCE là nơi lưu trữ duy nhất (singleton) của database trong toàn app.
     * <p>
     * Từ khóa "volatile" đảm bảo rằng:
     * - Mọi thread đều thấy cùng một phiên bản INSTANCE mới nhất.
     * - Khi một thread khởi tạo INSTANCE, các thread khác sẽ không dùng bản cũ bị cache.
     * <p>
     * 👉 Nói cách khác: volatile giúp tránh lỗi "cùng lúc khởi tạo nhiều database" khi đa luồng.
     */
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract LessonDao lessonDao();

    public abstract QuestionDao questionDao();

    /**
     * Hàm getInstance() trả về instance duy nhất của database.
     * Dùng double-checked locking để tránh tạo nhiều instance cùng lúc.
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) { // Bước 1: kiểm tra nhanh (không khóa)
            synchronized (AppDatabase.class) { // Bước 2: khóa class để đảm bảo thread an toàn
                if (INSTANCE == null) { // Bước 3: kiểm tra lại trong vùng synchronized
                    // Tạo database mới
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "appthilaixe.db" // Tên file database
                            )
                            // Khi nâng version DB mà chưa có migration, xóa DB cũ tạo lại (cẩn thận vì mất dữ liệu!)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
