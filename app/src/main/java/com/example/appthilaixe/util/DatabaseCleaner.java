package com.example.appthilaixe.util;

import android.content.Context;
import com.example.appthilaixe.database.AppDatabase;

/**
 * Tiện ích dọn toàn bộ dữ liệu trong DB (ngoại trừ bảng users).
 * Dùng khi cần reset dữ liệu seed.
 */
public class DatabaseCleaner {

    public static void clearAllExceptUsers(Context context) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);

            try {
                //  Xoá theo thứ tự để tránh lỗi khóa ngoại (nếu có)
                db.testQuestionDao().deleteAll();
//                db.userTestResultDao().deleteAll();
                db.questionDao().deleteAll();
//                db.lessonDao().deleteAll();
                db.testDao().deleteAll();
//                db.studyDao().deleteAll();

                System.out.println("Đã xoá toàn bộ dữ liệu (ngoại trừ bảng users)");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("⚠ Lỗi khi xoá dữ liệu: " + e.getMessage());
            }
        }).start();
    }
}
