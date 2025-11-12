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

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Question;
import com.example.appthilaixe.util.ProgressManager;

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

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";

    private int userId = 1; // ✅ user tạm

    private Map<Integer, String> userAnswers = new HashMap<>();
    private int correctAnswersCount = 0;
    private int wrongAnswersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_questions);

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
            Toast.makeText(this, "Không có câu hỏi nào", Toast.LENGTH_SHORT).show();
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
        AppDatabase db = AppDatabase.getInstance(this);
        questions = db.questionDao().getByCategory(categoryId);

        if (questions == null) {
            questions = new ArrayList<>();
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

        Question q = questions.get(currentQuestionIndex);

        tvQuestion.setText(q.getQuestionText());
        tvOptionA.setText(q.getOptionA());
        tvOptionB.setText(q.getOptionB());
        tvOptionC.setText(q.getOptionC());
        tvOptionD.setText(q.getOptionD());


            int imageRes = getResources().getIdentifier(q.getImageResId(), "drawable", getPackageName());
            if (imageRes != 0) {
                ivQuestionImage.setVisibility(View.VISIBLE);
                ivQuestionImage.setImageResource(imageRes);
            } else {
                ivQuestionImage.setVisibility(View.GONE);
            }


        updateProgress();
        clearSelection();
        updateNavigationButtons();

        ProgressManager.updateProgress(
                this,
                userId,
                categoryId,
                currentQuestionIndex,
                questions.size()
        );
    }

    private void selectAnswer(String answer) {
        selectedAnswer = answer;
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
        if (currentQuestionIndex >= questions.size() - 1) {
            navigateToResult();
            return;
        }
        currentQuestionIndex++;
        displayQuestion();
    }

    private void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion();
        }
    }

    private void updateProgress() {
        int current = currentQuestionIndex + 1;
        int total = questions.size();

        tvProgress.setText(current + "/" + total);
        progressBar.setProgress((current * 100) / total);
    }

    private void updateNavigationButtons() {
        btnPrevious.setEnabled(currentQuestionIndex > 0);
        btnPrevious.setAlpha(currentQuestionIndex > 0 ? 1f : 0.5f);

        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setText("Hoàn Thành");
        } else {
            btnNext.setText(R.string.next);
        }
    }

    private void navigateToResult() {
        calculateAnswers();

        Intent intent = new Intent(this, CategoryQuestionResultActivity.class);
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

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String ans = userAnswers.get(i);

            if (ans != null && !ans.isEmpty()) {
                if (q.isCorrectAnswer(ans)) correctAnswersCount++;
                else wrongAnswersCount++;
            } else {
                wrongAnswersCount++;
            }
        }
    }
}
