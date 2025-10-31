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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        Button btnStartExam = findViewById(R.id.btn_start_exam);
        Button btnLearnNow = findViewById(R.id.btn_learn_now);

        // Set click listeners
        btnStartExam.setOnClickListener(v -> {
            // Navigate to exam activity
            Intent intent = new Intent(HomeActivity.this, ExamActivity.class);
            startActivity(intent);
        });

        btnLearnNow.setOnClickListener(v -> {
            // Navigate to learning activity
            Intent intent = new Intent(HomeActivity.this, LearningActivity.class);
            startActivity(intent);
        });
    }
}

