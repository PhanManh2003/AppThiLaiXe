package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExamActivity extends AppCompatActivity {

    private TextView tvTimer, tvProgress, tvQuestion;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private LinearLayout optionA, optionB, optionC, optionD;
    private ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;
    private Button btnPrevious, btnNext;
    private ImageView btnBack;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private int currentQuestion = 1;
    private int totalQuestions = 40;
    private String selectedAnswer = "";
    private long timeLeftInMillis = 45 * 60 * 1000; // 45 minutes
    private long initialTimeInMillis = 45 * 60 * 1000; // Store initial time

    // Track answers for result calculation
    private int correctAnswersCount = 0;
    private int wrongAnswersCount = 0;
    private int skippedQuestionsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exam);

        initViews();
        setClickListeners();
        loadQuestion();
        startTimer();
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_timer);
        tvProgress = findViewById(R.id.tv_progress);
        tvQuestion = findViewById(R.id.tv_question);
        tvOptionA = findViewById(R.id.tv_option_a);
        tvOptionB = findViewById(R.id.tv_option_b);
        tvOptionC = findViewById(R.id.tv_option_c);
        tvOptionD = findViewById(R.id.tv_option_d);
        optionA = findViewById(R.id.option_a);
        optionB = findViewById(R.id.option_b);
        optionC = findViewById(R.id.option_c);
        optionD = findViewById(R.id.option_d);
        ivCheckA = findViewById(R.id.iv_check_a);
        ivCheckB = findViewById(R.id.iv_check_b);
        ivCheckC = findViewById(R.id.iv_check_c);
        ivCheckD = findViewById(R.id.iv_check_d);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setClickListeners() {
        btnBack.setOnClickListener(v -> {
            stopTimer();
            finish();
        });

        optionA.setOnClickListener(v -> selectAnswer("A"));
        optionB.setOnClickListener(v -> selectAnswer("B"));
        optionC.setOnClickListener(v -> selectAnswer("C"));
        optionD.setOnClickListener(v -> selectAnswer("D"));

        btnPrevious.setOnClickListener(v -> previousQuestion());
        btnNext.setOnClickListener(v -> nextQuestion());
    }

    private void loadQuestion() {
        // TODO: Load question from database
        tvQuestion.setText("Biển báo này có ý nghĩa gì?");
        tvOptionA.setText("Cấm đi vào");
        tvOptionB.setText("Cấm dừng");
        tvOptionC.setText("Cấm quay đầu");
        tvOptionD.setText("Cấm rẽ phải");

        updateProgress();
        clearSelection();
        updateNavigationButtons();
    }

    private void updateNavigationButtons() {
        // Update Previous button
        btnPrevious.setEnabled(currentQuestion > 1);
        btnPrevious.setAlpha(currentQuestion > 1 ? 1.0f : 0.5f);

        // Update Next button text
        if (currentQuestion == totalQuestions) {
            btnNext.setText("Hoàn Thành");
        } else {
            btnNext.setText(R.string.next);
        }
    }

    private void selectAnswer(String answer) {
        selectedAnswer = answer;
        clearSelection();

        switch (answer) {
            case "A":
                optionA.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckA.setVisibility(View.VISIBLE);
                break;
            case "B":
                optionB.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckB.setVisibility(View.VISIBLE);
                break;
            case "C":
                optionC.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckC.setVisibility(View.VISIBLE);
                break;
            case "D":
                optionD.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckD.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void clearSelection() {
        optionA.setBackgroundResource(R.drawable.answer_option_bg);
        optionB.setBackgroundResource(R.drawable.answer_option_bg);
        optionC.setBackgroundResource(R.drawable.answer_option_bg);
        optionD.setBackgroundResource(R.drawable.answer_option_bg);
        ivCheckA.setVisibility(View.GONE);
        ivCheckB.setVisibility(View.GONE);
        ivCheckC.setVisibility(View.GONE);
        ivCheckD.setVisibility(View.GONE);
    }

    private void nextQuestion() {
        // Check if we're at the last question
        if (currentQuestion >= totalQuestions) {
            // Already at last question, finish exam
            stopTimer();
            navigateToResult();
            return;
        }

        // Move to next question
        currentQuestion++;
        loadQuestion();

        // If we just moved to the last question, change button text
        if (currentQuestion == totalQuestions) {
            btnNext.setText("Hoàn Thành");
        }
    }

    private void previousQuestion() {
        if (currentQuestion > 1) {
            currentQuestion--;
            loadQuestion();
            updateNavigationButtons();
        }
    }

    private void updateProgress() {
        tvProgress.setText(currentQuestion + "/" + totalQuestions);
        int progress = (currentQuestion * 100) / totalQuestions;
        progressBar.setProgress(progress);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                Toast.makeText(ExamActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                // Navigate to result screen
                navigateToResult();
            }
        }.start();
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftText = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftText);
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void navigateToResult() {
        // Calculate time taken
        long timeTakenInMillis = initialTimeInMillis - timeLeftInMillis;
        int minutes = (int) (timeTakenInMillis / 1000) / 60;
        int seconds = (int) (timeTakenInMillis / 1000) % 60;
        String timeTaken = String.format("%02d:%02d", minutes, seconds);

        // TODO: In a real implementation, calculate actual correct/wrong/skipped answers
        // For now, use sample data based on random simulation
        correctAnswersCount = (int) (totalQuestions * 0.75); // 75% correct as sample
        wrongAnswersCount = totalQuestions - correctAnswersCount;
        skippedQuestionsCount = 0;

        // Create intent and pass data
        Intent intent = new Intent(ExamActivity.this, ExamResultActivity.class);
        intent.putExtra("total_questions", totalQuestions);
        intent.putExtra("correct_answers", correctAnswersCount);
        intent.putExtra("wrong_answers", wrongAnswersCount);
        intent.putExtra("skipped_questions", skippedQuestionsCount);
        intent.putExtra("time_taken", timeTaken);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}

