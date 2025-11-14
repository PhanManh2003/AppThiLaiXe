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

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.AnswerReview;
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
    private String selectedAnswer = "";
    private long timeLeftInMillis = 45 * 60 * 1000; // 45 phút
    private long initialTimeInMillis = 45 * 60 * 1000;

    // Track answers
    private int correctAnswersCount = 0;
    private int wrongAnswersCount = 0;
    private int skippedQuestionsCount = 0;

    // Store questions and user answers
    private List<Question> questions;
    private Map<Integer, String> userAnswers = new HashMap<>();

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
        if (questions == null || questions.isEmpty()) return;

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
        btnNext.setText(currentQuestion == totalQuestions ? "Hoàn Thành" : getString(R.string.next));
    }

    private void selectAnswer(String answer) {
        // Lưu đáp án người dùng vừa chọn vào biến tạm của activity
        selectedAnswer = answer;
        // Lưu đáp án này vào Map để theo dõi tất cả các câu hỏi
        // key = index của câu hỏi (currentQuestion - 1 vì list bắt đầu từ 0)
        // value = đáp án đã chọn ("A", "B", "C" hoặc "D")
        userAnswers.put(currentQuestion - 1, answer);
        // Xóa tất cả các highlight / check mark cũ
        clearSelection();

        switch (answer) {
            // Tô màu và hiển thị dấu check cho đáp án vừa chọn
            case "A": optionA.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckA.setVisibility(View.VISIBLE);
                break;

            case "B": optionB.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckB.setVisibility(View.VISIBLE);
                break;

            case "C": optionC.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckC.setVisibility(View.VISIBLE);
                break;

            case "D": optionD.setBackgroundResource(R.drawable.answer_option_selected);
                ivCheckD.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void clearSelection() {
        // Reset lại background của tất cả các option về trạng thái mặc định
        optionA.setBackgroundResource(R.drawable.answer_option_bg);
        optionB.setBackgroundResource(R.drawable.answer_option_bg);
        optionC.setBackgroundResource(R.drawable.answer_option_bg);
        optionD.setBackgroundResource(R.drawable.answer_option_bg);

        // Ẩn tất cả các dấu check
        ivCheckA.setVisibility(View.GONE);
        ivCheckB.setVisibility(View.GONE);
        ivCheckC.setVisibility(View.GONE);
        ivCheckD.setVisibility(View.GONE);
    }

    private void nextQuestion() {
        if (currentQuestion >= totalQuestions) {  // Kiểm tra xem đã là câu hỏi cuối cùng chưa
            stopTimer();                        // Nếu là câu cuối cùng, dừng đồng hồ
            navigateToResult();                  // Chuyển sang màn hình kết quả
            return;                              // Thoát phương thức
        }

        currentQuestion++;   // Chưa phải câu cuối, tăng số câu hỏi hiện tại lên 1
        loadQuestion();      // Tải câu hỏi mới lên giao diện
    }

    private void previousQuestion() {
        if (currentQuestion > 1) { // Chỉ cho phép quay lại nếu chưa phải câu hỏi đầu tiên
            currentQuestion--; // Giảm số câu hỏi hiện tại
            loadQuestion(); // Tải câu hỏi trước đó lên giao diện
        }
    }

    private void updateProgress() {
        // Hiển thị số câu hiện tại trên tổng số câu hỏi (VD: 5/40)
        tvProgress.setText(currentQuestion + "/" + totalQuestions);
        // Tính phần trăm tiến trình làm bài (VD: câu 20/40 → 50%)
        int progress = (currentQuestion * 100) / totalQuestions;
        // Cập nhật giá trị cho thanh ProgressBar
        progressBar.setProgress(progress);
    }

    private void startTimer() {
        // Tạo bộ đếm thời gian đếm ngược (CountDownTimer)
        //Cứ 1 giây, phương thức onTick(long millisUntilFinished) sẽ được gọi một lần.
        //Mỗi lần gọi, có thể cập nhật giao diện (ví dụ: hiển thị thời gian còn lại).
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Cập nhật thời gian còn lại mỗi giây
                timeLeftInMillis = millisUntilFinished;

                // Tính phút và giây còn lại
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;

                // Hiển thị thời gian theo định dạng mm:ss
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                // Khi hết thời gian, thông báo cho người dùng
                Toast.makeText(ExamActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                // Chuyển sang màn hình kết quả bài thi
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

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String userAnswer = userAnswers.get(i);

            if (userAnswer != null && !userAnswer.isEmpty()) {
                if (q.isCorrectAnswer(userAnswer)) correctAnswersCount++;
                else wrongAnswersCount++;
            } else skippedQuestionsCount++;
        }
    }

    private void navigateToResult() {
        calculateAnswers();

        // Tạo list AnswerReview
        ArrayList<AnswerReview> answerReviews = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String userAnswer = userAnswers.get(i);

            AnswerReview review = new AnswerReview(
                    q.getQuestionId(),
                    q.getQuestionTitle(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getOptionD(),
                    q.getCorrectAnswer(),
                    q.getExplanation(),
                    q.getImagePath(),
                    userAnswer
            );
            answerReviews.add(review);
        }

        long timeTakenMillis = initialTimeInMillis - timeLeftInMillis;
        int minutes = (int) (timeTakenMillis / 1000) / 60;
        int seconds = (int) (timeTakenMillis / 1000) % 60;
        String timeTaken = String.format("%02d:%02d", minutes, seconds);

        Intent intent = new Intent(ExamActivity.this, ExamResultActivity.class);
        intent.putExtra("total_questions", totalQuestions);
        intent.putExtra("correct_answers", correctAnswersCount);
        intent.putExtra("wrong_answers", wrongAnswersCount);
        intent.putExtra("skipped_questions", skippedQuestionsCount);
        intent.putExtra("time_taken", timeTaken);

        // Truyền list AnswerReview
        intent.putExtra("answer_reviews", answerReviews);

        // Truyền testId để ExamResultActivity có thể "thi lại" nếu muốn
        int testId = getIntent().getIntExtra("testId", 1);
        intent.putExtra("testId", testId);

        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

// code cũ
//    private void navigateToResult() {
//        // Calculate time taken
//        long timeTakenInMillis = initialTimeInMillis - timeLeftInMillis;
//        int minutes = (int) (timeTakenInMillis / 1000) / 60;
//        int seconds = (int) (timeTakenInMillis / 1000) % 60;
//        String timeTaken = String.format("%02d:%02d", minutes, seconds);
//
//        // Calculate actual correct/wrong/skipped answers
//        calculateAnswers();
//
//        // Create answer review list
//        ArrayList<AnswerReview> answerReviews = new ArrayList<>();
//        for (int i = 0; i < questions.size(); i++) {
//            Question question = questions.get(i);
//            String userAnswer = userAnswers.get(i);
//        }
//
//        // Create intent and pass data
//        Intent intent = new Intent(ExamActivity.this, ExamResultActivity.class);
//        intent.putExtra("total_questions", totalQuestions);
//        intent.putExtra("correct_answers", correctAnswersCount);
//        intent.putExtra("wrong_answers", wrongAnswersCount);
//        intent.putExtra("skipped_questions", skippedQuestionsCount);
//        intent.putExtra("time_taken", timeTaken);
//        intent.putExtra("answer_reviews", answerReviews);
//
//        startActivity(intent);
//        finish();
//    }

//    private void calculateAnswers() {
//        correctAnswersCount = 0;
//        wrongAnswersCount = 0;
//        skippedQuestionsCount = 0;
//
//        // Check each question
//        for (int i = 0; i < questions.size(); i++) {
//            Question question = questions.get(i);
//            String userAnswer = userAnswers.get(i);
//
//            if (userAnswer != null && !userAnswer.isEmpty()) {
//                if (question.isCorrectAnswer(userAnswer)) {
//                    correctAnswersCount++;
//                } else {
//                    wrongAnswersCount++;
//                }
//            } else {
//                // No answer selected counts as skipped
//                skippedQuestionsCount++;
//            }
//        }
//    }


}

