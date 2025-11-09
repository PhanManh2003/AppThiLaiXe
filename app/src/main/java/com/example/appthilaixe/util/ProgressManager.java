package com.example.appthilaixe.util;

import android.content.Context;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.database.LearningProgressDao;
import com.example.appthilaixe.database.LearningProgressEntity;

public class ProgressManager {

    public static void updateProgress(Context context, int userId, int categoryId, int lastQuestionIndex, int totalQuestions) {

        AppDatabase db = AppDatabase.getInstance(context);
        LearningProgressDao dao = db.learningProgressDao();

        LearningProgressEntity p = dao.getOne(userId, categoryId);
        long now = System.currentTimeMillis();

        int learned = Math.max(
                (p != null ? p.learnedCount : 0),
                lastQuestionIndex + 1
        );

        if (p == null) {
            p = new LearningProgressEntity(
                    userId,
                    categoryId,
                    learned,
                    lastQuestionIndex,
                    now
            );
            dao.save(p);
        } else {
            dao.updateCounts(
                    userId,
                    categoryId,
                    learned,
                    lastQuestionIndex,
                    now
            );
        }
    }
}
