package com.example.appthilaixe.util;

import android.content.Context;

import com.example.appthilaixe.dao.StudyDao;
import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Study;

public class ProgressManager {


    public static void updateProgress(Context context,
                                      int userId,
                                      int lessonId,
                                      int currentIndex) {

        AppDatabase db = AppDatabase.getInstance(context);
        StudyDao dao = db.studyDao();

        Study existing = dao.getStudy(userId, lessonId);


        int learned = currentIndex + 1;

        if (existing != null) {

            learned = Math.max(existing.learnedCount, learned);

            existing.learnedCount = learned;

            dao.insertOrUpdate(existing);

        } else {
            Study study = new Study();
            study.userId = userId;
            study.lessonId = lessonId;
            study.learnedCount = learned;

            dao.insertOrUpdate(study);
        }
    }


    public static int getProgress(Context context, int userId, int lessonId) {
        AppDatabase db = AppDatabase.getInstance(context);
        StudyDao dao = db.studyDao();

        Study s = dao.getStudy(userId, lessonId);
        return (s == null) ? 0 : s.learnedCount;
    }
}
