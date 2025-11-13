package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnStartExam = findViewById(R.id.btn_start_exam);

        // Set click listeners
        btnStartExam.setOnClickListener(v -> {
            // Navigate to exam activity
            Intent intent = new Intent(HomeActivity.this, ExamActivity.class);
            startActivity(intent);
        });

        int userId = getIntent().getIntExtra("userId", -1);

        Button btnLearnNow = findViewById(R.id.btn_learn_now);

        btnLearnNow.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LearningActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

    }
}

