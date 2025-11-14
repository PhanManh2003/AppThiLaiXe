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

import com.bumptech.glide.Glide;
import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Question;
import com.example.appthilaixe.util.ProgressManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryQuestionsActivity extends AppCompatActivity {

    private TextView tvCategoryName, tvProgress, tvQuestion;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private LinearLayout optionA, optionB, optionC, optionD;
    private ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;
    private ImageView ivQuestionImage;
    private ProgressBar progressBar;
    private Button btnPrevious, btnNext;
    private ImageView btnBack;

    private LinearLayout layoutExplanation;
    private TextView tvCorrectAnswer, tvExplanation;

    private int categoryId;
    private String categoryName;
    private int userId = 1;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private TextView tvQuestionNumber;

    private int correctCount = 0;
    private int wrongCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_questions);

        categoryId = getIntent().getIntExtra("lessonId", -1);
        categoryName = getIntent().getStringExtra("lessonName");

        if (categoryId == -1) {
            Toast.makeText(this, "Không tìm thấy danh mục!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadQuestions();
        setClickListeners();

        tvCategoryName.setText(categoryName);

        if (!questions.isEmpty()) {
            displayQuestion();
        }
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
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
        progressBar = findViewById(R.id.progress_bar);

        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);

        layoutExplanation = findViewById(R.id.layout_explanation);
        tvCorrectAnswer = findViewById(R.id.tv_correct_answer);
        tvExplanation = findViewById(R.id.tv_explanation);
    }

    private void loadQuestions() {
        AppDatabase db = AppDatabase.getInstance(this);
        questions = db.questionDao().getByLesson(categoryId);
        if (questions == null) questions = new ArrayList<>();
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

    private void selectAnswer(String selected) {
        Question q = questions.get(currentIndex);
        String correct = q.correctAnswer.trim().toUpperCase();

        clearSelection();

        if (selected.equals(correct)) {
            setOptionColor(selected, true);
            correctCount++;
        } else {
            setOptionColor(selected, false);
            wrongCount++;
        }

        highlightCorrectAnswer(correct);
        showExplanation(q);
        disableOptions();
    }

    private void setOptionColor(String answer, boolean isCorrect) {
        int bg = isCorrect ? R.drawable.answer_option_correct
                : R.drawable.answer_option_wrong;

        ImageView icon;

        switch (answer) {
            case "A": optionA.setBackgroundResource(bg); icon = ivCheckA; break;
            case "B": optionB.setBackgroundResource(bg); icon = ivCheckB; break;
            case "C": optionC.setBackgroundResource(bg); icon = ivCheckC; break;
            default:  optionD.setBackgroundResource(bg); icon = ivCheckD;
        }

        icon.setVisibility(View.VISIBLE);
    }

    private void highlightCorrectAnswer(String correct) {
        int bg = R.drawable.answer_option_correct;

        switch (correct) {
            case "A": optionA.setBackgroundResource(bg); break;
            case "B": optionB.setBackgroundResource(bg); break;
            case "C": optionC.setBackgroundResource(bg); break;
            case "D": optionD.setBackgroundResource(bg); break;
        }
    }

    private void showExplanation(Question q) {
        tvCorrectAnswer.setText("Đáp án đúng: " + q.correctAnswer);
        tvExplanation.setText(q.explanation);
        layoutExplanation.setVisibility(View.VISIBLE);
    }

    private void disableOptions() {
        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);
    }

    private void enableOptions() {
        optionA.setEnabled(true);
        optionB.setEnabled(true);
        optionC.setEnabled(true);
        optionD.setEnabled(true);
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

    private void displayQuestion() {
        tvQuestionNumber.setText("Câu " + (currentIndex + 1));

        if (questions.isEmpty()) return;

        Question q = questions.get(currentIndex);

        tvQuestion.setText(q.questionTitle);
        tvOptionA.setText(q.optionA);
        tvOptionB.setText(q.optionB);
        tvOptionC.setText(q.optionC);
        tvOptionD.setText(q.optionD);

        if (q.imagePath != null && !q.imagePath.trim().isEmpty()) {
            ivQuestionImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(q.imagePath).into(ivQuestionImage);
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        layoutExplanation.setVisibility(View.GONE);
        clearSelection();
        enableOptions();

        updateProgressBar();
        updateButtons();

        ProgressManager.updateProgress(this, userId, categoryId, currentIndex);
    }

    private void nextQuestion() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            displayQuestion();
        } else {
            openResultScreen();

        }
    }

    private void previousQuestion() {
        if (currentIndex > 0) {
            currentIndex--;
            displayQuestion();
        }
    }

    private void openResultScreen() {
        Intent intent = new Intent(CategoryQuestionsActivity.this, CategoryQuestionResultActivity.class);

        intent.putExtra("category_id", categoryId);
        intent.putExtra("category_name", categoryName);
        intent.putExtra("total_questions", questions.size());
        intent.putExtra("correct_answers", correctCount);
        intent.putExtra("wrong_answers", wrongCount);
        intent.putExtra("userId", userId);

        startActivity(intent);
        finish();
    }

    private void updateProgressBar() {
        int current = currentIndex + 1;
        int total = questions.size();

        tvProgress.setText(current + "/" + total);
        progressBar.setProgress((current * 100) / total);
    }

    private void updateButtons() {
        btnPrevious.setEnabled(currentIndex > 0);
        btnPrevious.setAlpha(currentIndex > 0 ? 1f : 0.5f);

        btnNext.setText(currentIndex == questions.size() - 1 ? "Hoàn thành" : "Tiếp tục");
    }
}
