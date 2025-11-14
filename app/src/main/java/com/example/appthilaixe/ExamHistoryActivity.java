package com.example.appthilaixe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.adapters.ExamHistoryAdapter;
import com.example.appthilaixe.dao.TestDao;
import com.example.appthilaixe.dao.UserTestResultDao;
import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Test;
import com.example.appthilaixe.models.UserTestResult;

import java.util.ArrayList;
import java.util.List;

public class ExamHistoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView rvExamHistory;
    private LinearLayout emptyState;

    private ExamHistoryAdapter adapter;
    private final List<ExamHistoryAdapter.ExamHistoryItem> historyItems = new ArrayList<>();

    private static final int PASS_PERCENTAGE = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_history);

        initViews();
        setupRecyclerView();
        loadHistoryFromDB();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        rvExamHistory = findViewById(R.id.rv_exam_history);
        emptyState = findViewById(R.id.empty_state);
    }

    private void setupRecyclerView() {
        // 👉 constructor ExamHistoryAdapter chỉ nhận List<ExamHistoryItem>
        adapter = new ExamHistoryAdapter(historyItems);
        rvExamHistory.setLayoutManager(new LinearLayoutManager(this));
        rvExamHistory.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Load toàn bộ lịch sử thi từ DB (không lọc user để tránh bị mất)
     */
    private void loadHistoryFromDB() {
        AppDatabase db = AppDatabase.getInstance(this);
        UserTestResultDao resultDao = db.userTestResultDao();
        TestDao testDao = db.testDao();

        List<UserTestResult> results = resultDao.getAll();   // luôn lấy tất cả

        historyItems.clear();

        if (results != null) {
            for (UserTestResult result : results) {
                Test test = testDao.getById(result.testId);
                String testName = test != null
                        ? test.getTestName()
                        : "Đề thi #" + result.testId;

                double percentage = result.result;
                boolean passed = percentage >= PASS_PERCENTAGE;

                historyItems.add(
                        new ExamHistoryAdapter.ExamHistoryItem(
                                result.testId,
                                testName,
                                percentage,
                                passed
                        )
                );
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (historyItems.isEmpty()) {
            rvExamHistory.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvExamHistory.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }
}
