package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appthilaixe.dao.UserTestResultDao;
import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.UserTestResult;

import java.util.ArrayList;

public class ExamResultActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivResultIcon;
    private TextView tvStatus, tvScore, tvPercentage, tvMessage;
    private TextView tvTotalQuestions, tvCorrectAnswers, tvWrongAnswers, tvSkippedQuestions;
    private TextView tvTimeTaken;
    private Button btnViewAnswers, btnRetake, btnHome;
    private CardView cardResult;

    // Exam data
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int skippedQuestions;
    private String timeTaken;
    private boolean isPassed;
    private int percentage;
    private ArrayList<AnswerReview> answerReviews;

    // Pass threshold (80% or 21/25 for standard driving test)
    private static final int PASS_PERCENTAGE = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exam_result);

        // Get exam results from intent
        getExamResults();

        // Initialize views
        initViews();

        // Calculate results
        calculateResults();

        // LƯU LỊCH SỬ THI (chỉ chạy lần đầu, tránh quay màn hình bị ghi trùng)
        int userId = getIntent().getIntExtra("userId", 1);   // nếu chưa truyền thì mặc định = 1
        int testId = getIntent().getIntExtra("testId", 1);   // nếu chỉ có 1 đề thì = 1
        if (savedInstanceState == null) {
            saveExamHistory(userId, testId, percentage);
        }

        // Display results
        displayResults();

        // Set click listeners
        setClickListeners();

        // Animate result card
        animateResultCard();
    }

    private void getExamResults() {
        Intent intent = getIntent();
        totalQuestions = intent.getIntExtra("total_questions", 30);
        correctAnswers = intent.getIntExtra("correct_answers", 0);
        wrongAnswers = intent.getIntExtra("wrong_answers", 0);
        skippedQuestions = intent.getIntExtra("skipped_questions", 0);
        timeTaken = intent.getStringExtra("time_taken");
        answerReviews = (ArrayList<AnswerReview>) intent.getSerializableExtra("answer_reviews");

        // Default time if not provided
        if (timeTaken == null || timeTaken.isEmpty()) {
            timeTaken = "00:00";
        }
        if (answerReviews == null) {
            answerReviews = new ArrayList<>();
        }
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
        btnViewAnswers = findViewById(R.id.btn_view_answers);
        btnRetake = findViewById(R.id.btn_retake);
        btnHome = findViewById(R.id.btn_home);
        cardResult = findViewById(R.id.card_result);
    }

    private void calculateResults() {
        // Calculate percentage
        if (totalQuestions > 0) {
            percentage = (correctAnswers * 100) / totalQuestions;
        } else {
            percentage = 0;
        }

        // Determine pass/fail
        isPassed = percentage >= PASS_PERCENTAGE;
    }

    private void displayResults() {
        // Display score
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvPercentage.setText(percentage + "%");

        // Display statistics
        tvTotalQuestions.setText(String.valueOf(totalQuestions));
        tvCorrectAnswers.setText(String.valueOf(correctAnswers));
        tvWrongAnswers.setText(String.valueOf(wrongAnswers));
        tvSkippedQuestions.setText(String.valueOf(skippedQuestions));
        tvTimeTaken.setText(timeTaken);

        // Display result based on pass/fail
        if (isPassed) {
            displayPassResult();
        } else {
            displayFailResult();
        }
    }

    private void displayPassResult() {
        // Set success icon and colors
        ivResultIcon.setImageResource(R.drawable.ic_success);
        tvStatus.setText("ĐẠT");
        tvStatus.setTextColor(0xFF4CAF50); // Green color
        cardResult.setCardBackgroundColor(0xFFFFFFFF); // White color

        // Set encouraging message
        if (percentage >= 95) {
            tvMessage.setText("Xuất sắc! Bạn đã làm bài rất tốt. Chúc mừng bạn đã vượt qua bài thi!");
        } else if (percentage >= 90) {
            tvMessage.setText("Tuyệt vời! Bạn đã vượt qua bài thi với kết quả cao. Chúc mừng bạn!");
        } else {
            tvMessage.setText("Chúc mừng! Bạn đã vượt qua bài thi. Tiếp tục cố gắng để đạt kết quả cao hơn!");
        }
    }

    private void displayFailResult() {
        // Set fail icon and colors
        ivResultIcon.setImageResource(R.drawable.ic_failed);
        tvStatus.setText("CHƯA ĐẠT");
        tvStatus.setTextColor(0xFFF44336); // Red color
        cardResult.setCardBackgroundColor(0xFFFFFFFF); // White color

        // Set encouraging message
        if (percentage >= 70) {
            tvMessage.setText("Bạn đã cố gắng rất tốt! Chỉ cần thêm một chút nữa là đạt. Hãy ôn tập và thử lại nhé!");
        } else if (percentage >= 50) {
            tvMessage.setText("Đừng nản lòng! Hãy xem lại đáp án, ôn tập kỹ hơn và thử lại. Bạn sẽ làm được!");
        } else {
            tvMessage.setText("Hãy dành thời gian ôn tập kỹ hơn. Xem lại các câu hỏi và học thêm. Chúc bạn may mắn lần sau!");
        }
    }

    private void setClickListeners() {
        // View answers button
        btnViewAnswers.setOnClickListener(v -> {
            if (answerReviews == null || answerReviews.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu đáp án để xem lại", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ExamResultActivity.this, AnswerReviewActivity.class);
            intent.putExtra("title", "Kết quả thi");
            intent.putExtra("answers", answerReviews);
            startActivity(intent);
        });

        // Retake exam button
        btnRetake.setOnClickListener(v -> {
            // Navigate back to exam activity
            Intent intent = new Intent(ExamResultActivity.this, ExamActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Home button
        btnHome.setOnClickListener(v -> {
            // Navigate to home activity
            Intent intent = new Intent(ExamResultActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void animateResultCard() {
        // Scale animation for result card
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.8f, 1.0f, // Start and end values for X axis
                0.8f, 1.0f, // Start and end values for Y axis
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y
        );
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        cardResult.startAnimation(scaleAnimation);

        // Fade in animation for icon
        ivResultIcon.setAlpha(0f);
        ivResultIcon.animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(200)
                .start();
    }

    @Override
    public void onBackPressed() {
        // Navigate to home when back is pressed
        Intent intent = new Intent(ExamResultActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    /**
     * Lưu lịch sử thi vào Room (bảng user_test_result)
     */
    private void saveExamHistory(int userId, int testId, int percentage) {
        if (userId <= 0 || testId <= 0) return;

        AppDatabase db = AppDatabase.getInstance(this);
        UserTestResultDao dao = db.userTestResultDao();

        UserTestResult result = new UserTestResult();
        result.userId = userId;
        result.testId = testId;
        result.result = percentage;

        dao.insert(result);
    }
}
