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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appthilaixe.models.AnswerReview;

import java.util.ArrayList;

public class CategoryQuestionResultActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivResultIcon;
    private TextView tvCategoryName, tvScore, tvPercentage, tvMessage;
    private TextView tvTotalQuestions, tvCorrectAnswers, tvWrongAnswers, tvMasteryLevel;
    private TextView tvCategoryProgress;
    private ProgressBar progressCategory;
    private Button btnReviewAnswers, btnContinueLearning, btnBackToCategories;
    private CardView cardResult;

    // Learning data
    private int categoryId;
    private String categoryName;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int percentage;
    private ArrayList<AnswerReview> answerReviews;

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
        answerReviews = (ArrayList<AnswerReview>) intent.getSerializableExtra("answer_reviews");

        // Default values if not provided
        if (categoryName == null || categoryName.isEmpty()) {
            categoryName = "Danh mục học tập";
        }
        if (answerReviews == null) {
            answerReviews = new ArrayList<>();
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
        btnReviewAnswers = findViewById(R.id.btn_review_answers);
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
        // Display score
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvPercentage.setText(percentage + "%");

        // Display statistics
        tvTotalQuestions.setText(String.valueOf(totalQuestions));
        tvCorrectAnswers.setText(String.valueOf(correctAnswers));
        tvWrongAnswers.setText(String.valueOf(wrongAnswers));

        // Set mastery level and message based on percentage
        String masteryLevel;
        String message;
        int masteryColor;

        if (percentage >= 90) {
            masteryLevel = "Xuất Sắc";
            masteryColor = 0xFF4CAF50; // Green
            message = "Tuyệt vời! Bạn đã nắm vững kiến thức về " + categoryName + ". Hãy tiếp tục phát huy!";
        } else if (percentage >= 80) {
            masteryLevel = "Tốt";
            masteryColor = 0xFF8BC34A; // Light Green
            message = "Rất tốt! Bạn đã hiểu khá rõ về " + categoryName + ". Tiếp tục cố gắng nhé!";
        } else if (percentage >= 70) {
            masteryLevel = "Khá";
            masteryColor = 0xFFFF9800; // Orange
            message = "Bạn đã làm khá tốt! Hãy xem lại một số câu để nắm vững hơn.";
        } else if (percentage >= 50) {
            masteryLevel = "Trung Bình";
            masteryColor = 0xFFFF9800; // Orange
            message = "Bạn cần ôn tập thêm về " + categoryName + ". Hãy xem lại đáp án và học lại nhé!";
        } else {
            masteryLevel = "Cần Cố Gắng";
            masteryColor = 0xFFF44336; // Red
            message = "Đừng nản lòng! Hãy dành thời gian học kỹ hơn về " + categoryName + ". Bạn sẽ tiến bộ!";
        }

        tvMasteryLevel.setText(masteryLevel);
        tvMasteryLevel.setTextColor(masteryColor);
        tvMessage.setText(message);

        // Update category progress
        progressCategory.setProgress(percentage);
        tvCategoryProgress.setText(percentage + "%");
    }

    private void setClickListeners() {
        // Review answers button
        btnReviewAnswers.setOnClickListener(v -> {
            if (answerReviews == null || answerReviews.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu đáp án để xem lại", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CategoryQuestionResultActivity.this, AnswerReviewActivity.class);
            intent.putExtra("title", categoryName);
            intent.putExtra("answers", answerReviews);
            startActivity(intent);
        });

        // Continue learning button - restart the same category
        btnContinueLearning.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryQuestionResultActivity.this, CategoryQuestionsActivity.class);
            intent.putExtra("category_id", categoryId);
            intent.putExtra("category_name", categoryName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Back to categories button
        btnBackToCategories.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryQuestionResultActivity.this, LearningActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void animateResultCard() {
        // Scale animation
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.8f, 1.0f, 0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        // Alpha animation
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);

        cardResult.startAnimation(scaleAnimation);
        cardResult.startAnimation(alphaAnimation);
    }

    @Override
    public void onBackPressed() {
        // Navigate back to learning activity
        Intent intent = new Intent(CategoryQuestionResultActivity.this, LearningActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}

