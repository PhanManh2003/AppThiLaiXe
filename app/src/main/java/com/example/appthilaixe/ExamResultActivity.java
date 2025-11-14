package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.UserTestResult;

import java.util.ArrayList;
import java.util.List;

public class ExamResultActivity extends AppCompatActivity {

    // UI components
    private ImageView ivResultIcon;
    private TextView tvStatus, tvScore, tvPercentage, tvMessage;
    private TextView tvTotalQuestions, tvCorrectAnswers, tvWrongAnswers, tvSkippedQuestions;
    private TextView tvTimeTaken;
    private Button btnRetake, btnHome;
    private CardView cardResult;

    // Data from intent
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int skippedQuestions;
    private String timeTaken;
    private ArrayList<AnswerReview> answerReviews;
    private int userId;
    private int testId;

    // Calculated
    private int percentage;
    private boolean isPassed;

    private static final int PASS_PERCENTAGE = 80;   // ngưỡng đậu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);

        // 1. Lấy dữ liệu từ ExamActivity / ExamHistoryActivity
        getExamResults();

        // 2. Ánh xạ view
        initViews();

        // 3. Tính toán kết quả
        calculateResults();

        // 4. Hiển thị kết quả
        displayResults();

        // 5. Lưu lịch sử thi (chỉ khi KHÔNG mở từ lịch sử)
        boolean fromHistory = getIntent().getBooleanExtra("from_history", false);
        if (!fromHistory && savedInstanceState == null) {
            saveExamHistory();
        }

        // 6. Gán click
        setupClickListeners();

        // 7. Animation nhẹ cho đẹp
        animateResultCard();
    }

    private void getExamResults() {
        Intent intent = getIntent();
        totalQuestions = intent.getIntExtra("total_questions", 0);
        correctAnswers = intent.getIntExtra("correct_answers", 0);
        wrongAnswers = intent.getIntExtra("wrong_answers", 0);
        skippedQuestions = intent.getIntExtra("skipped_questions", 0);
        timeTaken = intent.getStringExtra("time_taken");
        answerReviews = (ArrayList<AnswerReview>) intent.getSerializableExtra("answer_reviews");
        userId = intent.getIntExtra("userId", -1);
        testId = intent.getIntExtra("testId", 1);

        if (timeTaken == null) timeTaken = "00:00";
        if (answerReviews == null) answerReviews = new ArrayList<>();
    }

    private void initViews() {
        ivResultIcon = findViewById(R.id.iv_result_icon);
        tvStatus = findViewById(R.id.tv_status);
        tvScore = findViewById(R.id.tv_score);
        tvPercentage = findViewById(R.id.tv_percentage);
        tvMessage = findViewById(R.id.tv_message);
        tvTotalQuestions = findViewById(R.id.tv_total_questions);
        tvCorrectAnswers = findViewById(R.id.tv_correct_answers);
        tvWrongAnswers = findViewById(R.id.tv_wrong_answers);
        tvSkippedQuestions = findViewById(R.id.tv_skipped_questions);
        tvTimeTaken = findViewById(R.id.tv_time_taken);
        btnRetake = findViewById(R.id.btn_retake);
        btnHome = findViewById(R.id.btn_home);
        cardResult = findViewById(R.id.card_result);
    }

    private void calculateResults() {
        if (totalQuestions > 0) {
            percentage = (correctAnswers * 100) / totalQuestions;
        } else {
            percentage = 0;
        }
        isPassed = percentage >= PASS_PERCENTAGE;
    }

    private void displayResults() {
        // điểm tổng
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvPercentage.setText(percentage + "%");

        // thống kê
        tvTotalQuestions.setText(String.valueOf(totalQuestions));
        tvCorrectAnswers.setText(String.valueOf(correctAnswers));
        tvWrongAnswers.setText(String.valueOf(wrongAnswers));
        tvSkippedQuestions.setText(String.valueOf(skippedQuestions));
        tvTimeTaken.setText(timeTaken);

        // đổi icon + màu + message theo đậu / rớt
        if (isPassed) {
            ivResultIcon.setImageResource(R.drawable.ic_success);
            tvStatus.setText("ĐẠT");
            tvMessage.setText("Chúc mừng! Bạn đã vượt qua bài thi. Tiếp tục luyện tập để đạt điểm cao hơn nhé!");
        } else {
            ivResultIcon.setImageResource(R.drawable.ic_failed);
            tvStatus.setText("CHƯA ĐẠT");
            tvMessage.setText("Bạn chưa đạt yêu cầu. Hãy xem lại các câu sai, ôn tập thêm và thử lại.");
        }
    }

    /**
     * Lưu lịch sử thi vào bảng user_test_result
     */
    private void saveExamHistory() {
        try {
            if (userId <= 0) {
                // nếu chưa có user đăng nhập thì bỏ qua, hoặc bạn có thể gán userId = 1 (demo)
                return;
            }

            AppDatabase db = AppDatabase.getInstance(this);
            UserTestResult result = new UserTestResult();
            result.userId = userId;
            result.testId = testId;
            result.result = percentage;   // lưu % điểm

            List<UserTestResult> list = new ArrayList<>();
            list.add(result);

            db.userTestResultDao().insertAll(list);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lưu lịch sử thi thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        // Thi lại
        btnRetake.setOnClickListener(v -> {
            Intent intent = new Intent(ExamResultActivity.this, ExamActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("testId", testId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Về trang chủ
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ExamResultActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void animateResultCard() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.8f, 1.0f,
                0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(400);
        scaleAnimation.setFillAfter(true);
        cardResult.startAnimation(scaleAnimation);

        ivResultIcon.setAlpha(0f);
        ivResultIcon.animate()
                .alpha(1f)
                .setDuration(600)
                .setStartDelay(150)
                .start();
    }

    @Override
    public void onBackPressed() {
        // back về Home cho đơn giản
        Intent intent = new Intent(ExamResultActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
