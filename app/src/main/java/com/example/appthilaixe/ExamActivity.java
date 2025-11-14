package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamActivity extends AppCompatActivity {

    private TextView tvTimer, tvProgress, tvQuestion;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private LinearLayout optionA, optionB, optionC, optionD;
    private ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;
    private Button btnPrevious, btnNext;
    private ImageView btnBack;
    private ProgressBar progressBar;
    private TextView questionNumber;

    private CountDownTimer countDownTimer;
    private int currentQuestion = 1;
    private int totalQuestions = 0; // Lấy từ DB
    private long timeLeftInMillis = 45 * 60 * 1000; // 45 phút
    private final long initialTimeInMillis = 45 * 60 * 1000;

    // Track answers
    private int correctAnswersCount = 0;
    private int wrongAnswersCount = 0;
    private int skippedQuestionsCount = 0;

    // Store questions and user answers
    private List<Question> questions;
    // key: index câu hỏi (0..n-1), value: "A"/"B"/"C"/"D"
    private final Map<Integer, String> userAnswers = new HashMap<>();

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        // Khởi tạo database
        db = AppDatabase.getInstance(this);

        // Khởi tạo các view
        initViews();

        // Load câu hỏi từ database
        loadQuestionsFromDB();

        // Thiết lập click listener
        setClickListeners();

        // Hiển thị câu hỏi đầu tiên
        loadQuestion();

        // Bắt đầu đồng hồ
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
        questionNumber = findViewById(R.id.question_number);
    }

    /**
     * Load câu hỏi từ database
     */
    private void loadQuestionsFromDB() {
        // Nhận testId được truyền từ HomeActivity hoặc nơi gọi
        int testId = getIntent().getIntExtra("testId", 1); // mặc định = 1 nếu chưa truyền

        // Lấy danh sách câu hỏi theo testId
        questions = db.questionDao().getQuestionsByTestId(testId);
        if (questions == null) {
            questions = new ArrayList<>();
        }
        totalQuestions = questions.size();

        if (totalQuestions == 0) {
            Toast.makeText(this, "Chưa có câu hỏi trong đề thi!", Toast.LENGTH_LONG).show();
        }
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
        if (questions == null || questions.isEmpty() || currentQuestion < 1 || currentQuestion > totalQuestions) {
            return;
        }

        Question question = questions.get(currentQuestion - 1);
        questionNumber.setText("Câu " + currentQuestion);

        // Hiển thị câu hỏi và các đáp án
        tvQuestion.setText(question.getQuestionTitle());
        tvOptionA.setText(question.getOptionA());
        tvOptionB.setText(question.getOptionB());
        tvOptionC.setText(question.getOptionC());
        tvOptionD.setText(question.getOptionD());

        updateProgress();
        clearSelection();

        // Restore previous answer nếu người dùng đã chọn
        String previousAnswer = userAnswers.get(currentQuestion - 1);
        if (previousAnswer != null) {
            selectAnswer(previousAnswer);
        }

        updateNavigationButtons();
    }

    private void updateNavigationButtons() {
        btnPrevious.setEnabled(currentQuestion > 1);
        btnPrevious.setAlpha(currentQuestion > 1 ? 1.0f : 0.5f);


        btnNext.setText(currentQuestion == totalQuestions
                ? "Hoàn thành"
                : getString(R.string.next));
    }

    private void selectAnswer(String answer) {
        // lưu lại đáp án cho câu hiện tại
        userAnswers.put(currentQuestion - 1, answer);

        clearSelection();

        switch (answer) {
            case "A":
                optionA.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckA.setVisibility(TextView.VISIBLE);
                break;
            case "B":
                optionB.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckB.setVisibility(TextView.VISIBLE);
                break;
            case "C":
                optionC.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckC.setVisibility(TextView.VISIBLE);
                break;
            case "D":
                optionD.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckD.setVisibility(TextView.VISIBLE);
                break;
        }
    }

    private void clearSelection() {
        optionA.setBackgroundResource(R.drawable.answer_option_bg);
        optionB.setBackgroundResource(R.drawable.answer_option_bg);
        optionC.setBackgroundResource(R.drawable.answer_option_bg);
        optionD.setBackgroundResource(R.drawable.answer_option_bg);

        ivCheckA.setVisibility(TextView.GONE);
        ivCheckB.setVisibility(TextView.GONE);
        ivCheckC.setVisibility(TextView.GONE);
        ivCheckD.setVisibility(TextView.GONE);
    }

    private void nextQuestion() {
        if (totalQuestions == 0) {
            return;
        }

        if (currentQuestion >= totalQuestions) {
            // Câu cuối → dừng timer và chuyển sang màn kết quả
            stopTimer();
            navigateToResult();
            return;
        }

        currentQuestion++;
        loadQuestion();
    }

    private void previousQuestion() {
        if (currentQuestion > 1) {
            currentQuestion--;
            loadQuestion();
        }
    }

    private void updateProgress() {
        if (totalQuestions <= 0) {
            tvProgress.setText("0/0");
            progressBar.setProgress(0);
            return;
        }
        tvProgress.setText(currentQuestion + "/" + totalQuestions);
        int progress = (currentQuestion * 100) / totalQuestions;
        progressBar.setProgress(progress);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(ExamActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                navigateToResult();
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
    }

    private void calculateAnswers() {
        correctAnswersCount = 0;
        wrongAnswersCount = 0;
        skippedQuestionsCount = 0;

        if (questions == null) return;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String userAnswer = userAnswers.get(i);

            if (userAnswer != null && !userAnswer.isEmpty()) {
                if (q.isCorrectAnswer(userAnswer)) {
                    correctAnswersCount++;
                } else {
                    wrongAnswersCount++;
                }
            } else {
                skippedQuestionsCount++;
            }
        }
    }

    /**
     * Chuyển sang ExamResultActivity và truyền đầy đủ dữ liệu + answerReviews
     */
    private void navigateToResult() {
        if (questions == null || questions.isEmpty()) {
            finish();
            return;
        }

        // Tính thời gian đã làm
        long timeTakenInMillis = initialTimeInMillis - timeLeftInMillis;
        int minutes = (int) (timeTakenInMillis / 1000) / 60;
        int seconds = (int) (timeTakenInMillis / 1000) % 60;
        String timeTaken = String.format("%02d:%02d", minutes, seconds);

        // Tính đúng/sai/bỏ qua
        calculateAnswers();

        // Tạo danh sách AnswerReview để xem lại đáp án
        ArrayList<AnswerReview> answerReviews = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers.get(i);

            boolean answered = userAnswer != null && !userAnswer.isEmpty();
            boolean correct = answered && question.isCorrectAnswer(userAnswer);

            // TODO: nếu model Question có sẵn text đáp án đúng thì thay chuỗi rỗng này
            String correctAnswerText = "";

            AnswerReview review = new AnswerReview(
                    i + 1,
                    question.getQuestionTitle(),
                    userAnswer,
                    correctAnswerText,
                    answered,
                    correct
            );
            answerReviews.add(review);
        }

        // Lấy userId & testId đã truyền từ HomeActivity
        int userId = getIntent().getIntExtra("userId", -1);
        int testId = getIntent().getIntExtra("testId", 1);

        // Gửi sang ExamResultActivity
        Intent intent = new Intent(ExamActivity.this, ExamResultActivity.class);
        intent.putExtra("total_questions", totalQuestions);
        intent.putExtra("correct_answers", correctAnswersCount);
        intent.putExtra("wrong_answers", wrongAnswersCount);
        intent.putExtra("skipped_questions", skippedQuestionsCount);
        intent.putExtra("time_taken", timeTaken);
        intent.putExtra("answer_reviews", answerReviews);
        intent.putExtra("userId", userId);
        intent.putExtra("testId", testId);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
