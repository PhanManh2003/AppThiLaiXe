package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CategoryQuestionResultActivity extends AppCompatActivity {

    private ImageView ivResultIcon;
    private TextView tvCategoryName, tvScore, tvPercentage, tvMessage;
    private TextView tvTotalQuestions, tvCorrectAnswers, tvWrongAnswers, tvMasteryLevel;
    private TextView tvCategoryProgress;
    private ProgressBar progressCategory;
    private Button btnContinueLearning, btnBackToCategories, btnReviewAnswers;
    private CardView cardResult;

    private int categoryId;
    private String categoryName;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_question_result);

        getLearningResults();
        initViews();
        calculateResults();
        displayResults();
        setClickListeners();
        animateResultCard();
    }

    private void getLearningResults() {
        Intent intent = getIntent();
        categoryId = intent.getIntExtra("category_id", -1);
        categoryName = intent.getStringExtra("category_name");
        totalQuestions = intent.getIntExtra("total_questions", 0);
        correctAnswers = intent.getIntExtra("correct_answers", 0);
        wrongAnswers = intent.getIntExtra("wrong_answers", 0);

        if (categoryName == null || categoryName.isEmpty()) {
            categoryName = "Danh mục học tập";
        }
    }

    private void initViews() {
        ivResultIcon = findViewById(R.id.iv_result_icon);
        tvCategoryName = findViewById(R.id.tv_category_name);
        tvScore = findViewById(R.id.tv_score);
        tvPercentage = findViewById(R.id.tv_percentage);
        tvMessage = findViewById(R.id.tv_message);
        tvTotalQuestions = findViewById(R.id.tv_total_questions);
        tvCorrectAnswers = findViewById(R.id.tv_correct_answers);
        tvWrongAnswers = findViewById(R.id.tv_wrong_answers);
        tvMasteryLevel = findViewById(R.id.tv_mastery_level);
        tvCategoryProgress = findViewById(R.id.tv_category_progress);
        progressCategory = findViewById(R.id.progress_category);

        btnReviewAnswers = findViewById(R.id.btn_review_answers);   // ⭐ NÚT XEM LẠI ĐÁP ÁN
        btnContinueLearning = findViewById(R.id.btn_continue_learning);
        btnBackToCategories = findViewById(R.id.btn_back_to_categories);

        cardResult = findViewById(R.id.card_result);

        tvCategoryName.setText(categoryName);
    }

    private void calculateResults() {
        if (totalQuestions > 0) {
            percentage = (correctAnswers * 100) / totalQuestions;
        } else {
            percentage = 0;
        }
    }

    private void displayResults() {
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvPercentage.setText(percentage + "%");

        tvTotalQuestions.setText(String.valueOf(totalQuestions));
        tvCorrectAnswers.setText(String.valueOf(correctAnswers));
        tvWrongAnswers.setText(String.valueOf(wrongAnswers));

        String masteryLevel;
        String message;
        int masteryColor;

        if (percentage >= 90) {
            masteryLevel = "Xuất Sắc";
            masteryColor = 0xFF4CAF50;
            message = "Tuyệt vời! Bạn đã nắm vững kiến thức.";
        } else if (percentage >= 80) {
            masteryLevel = "Tốt";
            masteryColor = 0xFF8BC34A;
            message = "Rất tốt! Tiếp tục phát huy nhé.";
        } else if (percentage >= 70) {
            masteryLevel = "Khá";
            masteryColor = 0xFFFF9800;
            message = "Bạn làm khá tốt!";
        } else if (percentage >= 50) {
            masteryLevel = "Trung Bình";
            masteryColor = 0xFFFF9800;
            message = "Cần ôn luyện thêm!";
        } else {
            masteryLevel = "Cần Cố Gắng";
            masteryColor = 0xFFF44336;
            message = "Đừng bỏ cuộc! Hãy học lại nhé!";
        }

        tvMasteryLevel.setText(masteryLevel);
        tvMasteryLevel.setTextColor(masteryColor);
        tvMessage.setText(message);

        progressCategory.setProgress(percentage);
        tvCategoryProgress.setText(percentage + "%");
    }

    private void setClickListeners() {

        // ⭐ NÚT XEM LẠI ĐÁP ÁN = QUAY LẠI MÀN TRƯỚC
        btnReviewAnswers.setOnClickListener(v -> {
            finish();  // QUAY LẠI CategoryQuestionsActivity
        });

        // Nút Học tiếp (cũng quay lại màn trước)
        btnContinueLearning.setOnClickListener(v -> finish());

        // Nút quay về danh mục bài học
        btnBackToCategories.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryQuestionResultActivity.this, LearningActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void animateResultCard() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.8f, 1.0f, 0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(500);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);

        cardResult.startAnimation(scaleAnimation);
        cardResult.startAnimation(alphaAnimation);
    }
}
