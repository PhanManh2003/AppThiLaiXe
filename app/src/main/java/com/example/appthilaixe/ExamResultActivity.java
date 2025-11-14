package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExamResultActivity extends AppCompatActivity {

    private TextView tvStatus, tvScore, tvPercentage, tvMessage;
    private TextView tvTotal, tvCorrect, tvWrong, tvSkipped, tvTime;
    private ImageView ivResultIcon;

    private Button btnViewAnswers, btnRetake, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);

        initViews();
        loadData();
        setListeners();
    }

    private void initViews() {
        ivResultIcon = findViewById(R.id.iv_result_icon);
        tvStatus = findViewById(R.id.tv_status);
        tvScore = findViewById(R.id.tv_score);
        tvPercentage = findViewById(R.id.tv_percentage);
        tvMessage = findViewById(R.id.tv_message);

        tvTotal = findViewById(R.id.tv_total_questions);
        tvCorrect = findViewById(R.id.tv_correct_answers);
        tvWrong = findViewById(R.id.tv_wrong_answers);
        tvSkipped = findViewById(R.id.tv_skipped_questions);
        tvTime = findViewById(R.id.tv_time_taken);

//        btnViewAnswers = findViewById(R.id.btn_view_answers);
        btnRetake = findViewById(R.id.btn_retake);
        btnHome = findViewById(R.id.btn_home);
    }

    private void loadData() {
        int total = getIntent().getIntExtra("total_questions", 0);
        int correct = getIntent().getIntExtra("correct_answers", 0);
        int wrong = getIntent().getIntExtra("wrong_answers", 0);
        int skipped = getIntent().getIntExtra("skipped_questions", 0);
        String timeTaken = getIntent().getStringExtra("time_taken");

        int percentage = (correct * 100) / total;

        tvScore.setText(correct + "/" + total);
        tvPercentage.setText(percentage + "%");
        tvTotal.setText(String.valueOf(total));
        tvCorrect.setText(String.valueOf(correct));
        tvWrong.setText(String.valueOf(wrong));
        tvSkipped.setText(String.valueOf(skipped));
        tvTime.setText(timeTaken);

        if (percentage >= 80) {
            tvStatus.setText("ĐẠT");
            ivResultIcon.setImageResource(R.drawable.ic_success);
            tvStatus.setTextColor(0xFF4CAF50);
            tvMessage.setText("Chúc mừng! Bạn đã vượt qua bài thi.");
        } else {
            tvStatus.setText("KHÔNG ĐẠT");
            ivResultIcon.setImageResource(R.drawable.ic_failed);
            tvStatus.setTextColor(0xFFF44336);
            tvMessage.setText("Bạn chưa đạt yêu cầu. Hãy thử lại nhé!");
        }
    }

    private void setListeners() {
        btnHome.setOnClickListener(v -> {
            finish();
        });

        btnRetake.setOnClickListener(v -> {

            // Lấy testId đã được truyền từ ExamActivity trước đó
            int testId = getIntent().getIntExtra("testId", 1);

            // Tạo Intent để mở lại màn thi
            Intent intent = new Intent(ExamResultActivity.this, ExamActivity.class);

            // Truyền testId để ExamActivity biết đề thi nào cần load
            intent.putExtra("testId", testId);

            // Chuyển sang màn thi
            startActivity(intent);

            // Đóng màn kết quả
            finish();
        });

    }
}
