package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.adapters.LessonAdapter;
import com.example.appthilaixe.dao.LessonDao;
import com.example.appthilaixe.dao.QuestionDao;
import com.example.appthilaixe.dao.StudyDao;
import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Lesson;
import com.example.appthilaixe.models.Question;
import com.example.appthilaixe.models.Study;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class LearningActivity extends AppCompatActivity implements LessonAdapter.OnLessonClickListener {

    private ImageView btnBack;
    private TextInputEditText etSearch;
    private RecyclerView rvLessons;
    private LinearLayout emptyState;

    private LessonAdapter adapter;

    private List<Lesson> allLessons = new ArrayList<>();
    private List<Lesson> filteredLessons = new ArrayList<>();

    // dữ liệu bổ sung
    private List<Question> allQuestions = new ArrayList<>();
    private List<Study> allStudies = new ArrayList<>();

    private int userId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning);

        initViews();
        setupRecyclerView();
        loadDataFromDB();
        setClickListeners();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDB();
    }


    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search);
        rvLessons = findViewById(R.id.rv_categories);
        emptyState = findViewById(R.id.empty_state);

        userId = getIntent().getIntExtra("userId", 1);
    }

    private void setupRecyclerView() {
        rvLessons.setLayoutManager(new LinearLayoutManager(this));

        // Adapter tạm, lát nữa set lại sau khi load DB
        adapter = new LessonAdapter(
                filteredLessons,
                new ArrayList<>(),
                new ArrayList<>(),
                userId,
                this
        );

        rvLessons.setAdapter(adapter);
    }

    private void loadDataFromDB() {
        AppDatabase db = AppDatabase.getInstance(this);

        // Load Lessons
        LessonDao lessonDao = db.lessonDao();
        allLessons = lessonDao.getAll();

        filteredLessons.clear();
        filteredLessons.addAll(allLessons);

        // Load Questions
        QuestionDao questionDao = db.questionDao();
        allQuestions = questionDao.getAll();


        StudyDao studyDao = db.studyDao();
        allStudies = studyDao.getAll();

        // tạo adapter mới sau khi có đủ dữ liệu
        adapter = new LessonAdapter(
                filteredLessons,
                allQuestions,
                allStudies,
                userId,
                this
        );

        rvLessons.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateEmptyState();
    }

    private void setClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLessons(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterLessons(String query) {
        filteredLessons.clear();

        if (query.isEmpty()) {
            filteredLessons.addAll(allLessons);
        } else {
            String lower = query.toLowerCase();
            for (Lesson l : allLessons) {
                if (l.lessonName.toLowerCase().contains(lower)) {
                    filteredLessons.add(l);
                }
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredLessons.isEmpty()) {
            rvLessons.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvLessons.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartClick(Lesson lesson) {
        Intent intent = new Intent(this, CategoryQuestionsActivity.class);
        intent.putExtra("lessonId", lesson.lessonId);
        intent.putExtra("lessonName", lesson.lessonName);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
