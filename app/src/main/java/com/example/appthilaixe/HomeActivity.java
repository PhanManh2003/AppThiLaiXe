package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnStartExam = findViewById(R.id.btn_start_exam);
        Button btnLearnNow = findViewById(R.id.btn_learn_now);
        Button btnExamHistory = findViewById(R.id.btn_exam_history);

        // userId nhận từ LoginActivity
        int userId = getIntent().getIntExtra("userId", -1);

        // Bắt đầu thi thử
        btnStartExam.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ExamActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Học ngay
        btnLearnNow.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LearningActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Lịch sử thi
        btnExamHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ExamHistoryActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}
