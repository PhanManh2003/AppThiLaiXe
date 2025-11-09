package com.example.appthilaixe.repositories;

import android.content.Context;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.database.CategoryEntity;
import com.example.appthilaixe.database.LearningProgressDao;
import com.example.appthilaixe.database.LearningProgressEntity;
import com.example.appthilaixe.database.QuestionDao;
import com.example.appthilaixe.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private final AppDatabase db;
    private final QuestionDao questionDao;
    private final LearningProgressDao progressDao;

    public CategoryRepository(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.questionDao = db.questionDao();
        this.progressDao = db.learningProgressDao();
    }

    public List<Category> loadWithProgress(int userId) {
        List<CategoryEntity> entities = db.categoryDao().getAllCategories();
        List<Category> result = new ArrayList<>();

        for (CategoryEntity e : entities) {
            int total = questionDao.getByCategory(e.getId()).size();
            LearningProgressEntity p = progressDao.getOne(userId, e.getId());
            int learned = (p != null) ? p.learnedCount : 0;

            Category c = new Category(
                    e.getId(),
                    e.getName(),
                    e.getDescription(),
                    0,
                    total,
                    learned,
                    0
            );

            result.add(c);
        }

        return result;
    }
}
