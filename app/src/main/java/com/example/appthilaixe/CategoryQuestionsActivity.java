package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.appthilaixe.models.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryQuestionsActivity extends AppCompatActivity {

    private TextView tvCategoryName, tvProgress, tvQuestion;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private LinearLayout optionA, optionB, optionC, optionD;
    private ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;
    private ImageView ivQuestionImage;
    private Button btnPrevious, btnNext;
    private ImageView btnBack;
    private ProgressBar progressBar;

    private int categoryId;
    private String categoryName;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";

    // Track user answers
    private Map<Integer, String> userAnswers = new HashMap<>();
    private int correctAnswersCount = 0;
    private int wrongAnswersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_questions);

        // Get category information from intent
        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        if (categoryId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadQuestions();
        setClickListeners();
        
        if (!questions.isEmpty()) {
            displayQuestion();
        } else {
            Toast.makeText(this, "Không có câu hỏi nào trong danh mục này", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        tvCategoryName = findViewById(R.id.tv_category_name);
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
        ivQuestionImage = findViewById(R.id.iv_question_image);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progress_bar);

        tvCategoryName.setText(categoryName);
    }

    private void loadQuestions() {
        // TODO: Load questions from database based on categoryId
        questions = new ArrayList<>();

        // Sample data - filter by category
        switch (categoryId) {
            case 1: // Biển báo giao thông
                questions.add(new Question(1, 1, "Biển báo này có ý nghĩa gì?", 
                    "Cấm đi vào", "Cấm dừng", "Cấm quay đầu", "Cấm rẽ phải", 
                    "A", "Biển báo cấm đi vào được sử dụng để cấm các phương tiện đi vào khu vực này."));
                questions.add(new Question(2, 1, "Biển báo hình tam giác viền đỏ có ý nghĩa gì?", 
                    "Biển cấm", "Biển báo nguy hiểm", "Biển chỉ dẫn", "Biển hiệu lệnh", 
                    "B", "Biển báo nguy hiểm có hình tam giác viền đỏ, cảnh báo người lái xe về các nguy hiểm phía trước."));
                questions.add(new Question(3, 1, "Biển báo hình tròn viền đỏ có ý nghĩa gì?", 
                    "Biển chỉ dẫn", "Biển cấm", "Biển báo nguy hiểm", "Biển hiệu lệnh", 
                    "B", "Biển cấm có hình tròn viền đỏ, cấm các hành vi được thể hiện trên biển."));
                break;
            case 2: // Luật giao thông
                questions.add(new Question(4, 2, "Tốc độ tối đa cho phép trong khu dân cư là bao nhiêu?", 
                    "40 km/h", "50 km/h", "60 km/h", "70 km/h", 
                    "B", "Tốc độ tối đa trong khu dân cư là 50 km/h theo quy định của luật giao thông."));
                questions.add(new Question(5, 2, "Khi tham gia giao thông, người lái xe phải mang theo giấy tờ gì?", 
                    "Chỉ cần bằng lái xe", "Bằng lái xe và giấy đăng ký xe", "Chỉ cần giấy đăng ký xe", "Không cần giấy tờ gì", 
                    "B", "Người lái xe phải mang theo bằng lái xe và giấy đăng ký xe khi tham gia giao thông."));
                break;
            case 3: // Kỹ năng lái xe
                questions.add(new Question(6, 3, "Khi lái xe trong điều kiện trời mưa, bạn nên làm gì?", 
                    "Tăng tốc độ", "Giảm tốc độ và tăng khoảng cách", "Giữ nguyên tốc độ", "Phanh gấp thường xuyên", 
                    "B", "Khi trời mưa, nên giảm tốc độ và tăng khoảng cách an toàn vì đường trơn trượt."));
                questions.add(new Question(7, 3, "Khi xuống dốc dài, bạn nên sử dụng phanh như thế nào?", 
                    "Phanh liên tục", "Phanh từng đoạn ngắn", "Không phanh", "Phanh mạnh một lần", 
                    "B", "Nên phanh từng đoạn ngắn để tránh phanh quá nóng và mất hiệu quả."));
                break;
            case 4: // Xử lý tình huống
                questions.add(new Question(8, 4, "Khi xe bị nổ lốp đột ngột, bạn nên làm gì?", 
                    "Phanh gấp ngay lập tức", "Giữ chặt vô lăng, giảm ga từ từ", "Đánh lái mạnh", "Tăng ga vượt qua", 
                    "B", "Khi nổ lốp, cần giữ chặt vô lăng và giảm ga từ từ, tránh phanh gấp."));
                questions.add(new Question(9, 4, "Khi gặp xe ưu tiên đang phát tín hiệu, bạn phải làm gì?", 
                    "Tiếp tục đi", "Giảm tốc độ và nhường đường", "Tăng tốc vượt qua", "Dừng lại ngay", 
                    "B", "Phải giảm tốc độ và nhường đường cho xe ưu tiên."));
                break;
            case 5: // Bảo dưỡng xe
                questions.add(new Question(10, 5, "Bạn nên kiểm tra áp suất lốp xe bao lâu một lần?", 
                    "Mỗi ngày", "Mỗi tuần", "Mỗi tháng", "Mỗi năm", 
                    "C", "Nên kiểm tra áp suất lốp xe mỗi tháng để đảm bảo an toàn."));
                questions.add(new Question(11, 5, "Dầu động cơ có tác dụng gì?", 
                    "Làm mát động cơ", "Bôi trơn và làm mát động cơ", "Chỉ làm sạch động cơ", "Tăng công suất", 
                    "B", "Dầu động cơ có tác dụng bôi trơn các bộ phận và làm mát động cơ."));
                break;
            default:
                // No questions for unknown category
                break;
        }
    }

    private void setClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        optionA.setOnClickListener(v -> selectAnswer("A"));
        optionB.setOnClickListener(v -> selectAnswer("B"));
        optionC.setOnClickListener(v -> selectAnswer("C"));
        optionD.setOnClickListener(v -> selectAnswer("D"));

        btnPrevious.setOnClickListener(v -> previousQuestion());
        btnNext.setOnClickListener(v -> nextQuestion());
    }

    private void displayQuestion() {
        if (questions.isEmpty()) return;

        Question question = questions.get(currentQuestionIndex);
        
        tvQuestion.setText(question.getQuestionText());
        tvOptionA.setText(question.getOptionA());
        tvOptionB.setText(question.getOptionB());
        tvOptionC.setText(question.getOptionC());
        tvOptionD.setText(question.getOptionD());

        // Show/hide question image
        if (question.hasImage()) {
            ivQuestionImage.setImageResource(question.getImageResId());
            ivQuestionImage.setVisibility(View.VISIBLE);
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        updateProgress();
        clearSelection();
        updateNavigationButtons();
    }

    private void selectAnswer(String answer) {
        selectedAnswer = answer;

        // Save user's answer
        userAnswers.put(currentQuestionIndex, answer);

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
        if (currentQuestionIndex >= questions.size() - 1) {
            // Already at last question, finish learning
            navigateToResult();
            return;
        }

        // Move to next question
        currentQuestionIndex++;
        selectedAnswer = "";
        displayQuestion();
    }

    private void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            selectedAnswer = "";
            displayQuestion();
        }
    }

    private void updateProgress() {
        int current = currentQuestionIndex + 1;
        int total = questions.size();
        tvProgress.setText(current + "/" + total);
        int progress = (current * 100) / total;
        progressBar.setProgress(progress);
    }

    private void updateNavigationButtons() {
        // Disable previous button on first question
        btnPrevious.setEnabled(currentQuestionIndex > 0);
        btnPrevious.setAlpha(currentQuestionIndex > 0 ? 1.0f : 0.5f);

        // Change next button text on last question
        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setText("Hoàn Thành");
        } else {
            btnNext.setText(R.string.next);
        }
    }

    private void navigateToResult() {
        // Calculate results
        calculateAnswers();

        // Create intent and pass data
        Intent intent = new Intent(CategoryQuestionsActivity.this, CategoryQuestionResultActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("category_name", categoryName);
        intent.putExtra("total_questions", questions.size());
        intent.putExtra("correct_answers", correctAnswersCount);
        intent.putExtra("wrong_answers", wrongAnswersCount);

        startActivity(intent);
        finish();
    }

    private void calculateAnswers() {
        correctAnswersCount = 0;
        wrongAnswersCount = 0;

        // Check each question
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers.get(i);

            if (userAnswer != null && !userAnswer.isEmpty()) {
                if (question.isCorrectAnswer(userAnswer)) {
                    correctAnswersCount++;
                } else {
                    wrongAnswersCount++;
                }
            } else {
                // No answer selected counts as wrong
                wrongAnswersCount++;
            }
        }
    }
}

