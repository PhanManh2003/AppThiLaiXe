package com.example.appthilaixe.database;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Database(
        entities = {
                User.class,
                CategoryEntity.class,
                QuestionEntity.class,
                ExamResultEntity.class,
                UserAnswerEntity.class,
                LearningProgressEntity.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract QuestionDao questionDao();
    public abstract LearningProgressDao learningProgressDao();

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
                            .addCallback(new SeedDatabaseCallback(context))
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class SeedDatabaseCallback extends RoomDatabase.Callback {
        private final Context context;

        SeedDatabaseCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(() -> seedDatabase(context)).start();
        }

        private void seedDatabase(Context context) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStream is = assetManager.open("seed_data.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) builder.append(line);
                reader.close();

                JSONObject json = new JSONObject(builder.toString());

                AppDatabase database = getInstance(context);

                // ===== Seed Users =====
                JSONArray users = json.getJSONArray("users");
                for (int i = 0; i < users.length(); i++) {
                    JSONObject u = users.getJSONObject(i);
                    User user = new User(
                            u.getString("username"),
                            u.getString("email"),
                            u.getString("password")
                    );
                    database.userDao().insertUser(user);
                }

                JSONArray categories = json.getJSONArray("categories");
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject c = categories.getJSONObject(i);
                    CategoryEntity cat = new CategoryEntity(
                            c.getString("name"),
                            c.getString("description")
                    );
                    database.categoryDao().insertCategory(cat);
                }

                JSONArray questions = json.getJSONArray("questions");
                for (int i = 0; i < questions.length(); i++) {
                    JSONObject q = questions.getJSONObject(i);
                    QuestionEntity question = new QuestionEntity(
                            0,
                            q.getInt("categoryId"),
                            q.getString("questionText"),
                            q.getString("optionA"),
                            q.getString("optionB"),
                            q.getString("optionC"),
                            q.getString("optionD"),
                            q.getString("correctAnswer"),
                            q.getString("imagePath"),
                            q.getString("explanation")
                    );
                    database.questionDao().insertQuestion(question);
                }

                System.out.println("Seed database completed!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
