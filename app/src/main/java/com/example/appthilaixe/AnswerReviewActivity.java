//package com.example.appthilaixe;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AnswerReviewActivity extends AppCompatActivity {
//
//    private ImageView btnBack;
//    private TextView tvTitle;
//    private TextView tvCorrectCount, tvWrongCount, tvSkippedCount;
//    private LinearLayout layoutSkipped;
//    private Button btnFilterAll, btnFilterCorrect, btnFilterWrong;
//    private RecyclerView rvAnswers;
//
//    private AnswerReviewAdapter adapter;
//    private List<AnswerReview> allAnswers;
//    private List<AnswerReview> filteredAnswers;
//    private String currentFilter = "ALL"; // ALL, CORRECT, WRONG
//
//    private String title;
//    private int correctCount = 0;
//    private int wrongCount = 0;
//    private int skippedCount = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_answer_review);
//
//        getDataFromIntent();
//        initViews();
//        setupRecyclerView();
//        displaySummary();
//        setClickListeners();
//        applyFilter("ALL");
//    }
//
//    private void getDataFromIntent() {
//        title = getIntent().getStringExtra("title");
//        if (title == null || title.isEmpty()) {
//            title = "Kết quả thi";
//        }
//
//        // Get the list of AnswerReview objects
//        allAnswers = (ArrayList<AnswerReview>) getIntent().getSerializableExtra("answers");
//        if (allAnswers == null) {
//            allAnswers = new ArrayList<>();
//        }
//
//        // Calculate counts
//        for (AnswerReview answer : allAnswers) {
//            if (answer.isCorrect()) {
//                correctCount++;
//            } else if (answer.isAnswered()) {
//                wrongCount++;
//            } else {
//                skippedCount++;
//            }
//        }
//    }
//
//    private void initViews() {
//        btnBack = findViewById(R.id.btn_back);
//        tvTitle = findViewById(R.id.tv_title);
//        tvCorrectCount = findViewById(R.id.tv_correct_count);
//        tvWrongCount = findViewById(R.id.tv_wrong_count);
//        tvSkippedCount = findViewById(R.id.tv_skipped_count);
//        layoutSkipped = findViewById(R.id.layout_skipped);
//        btnFilterAll = findViewById(R.id.btn_filter_all);
//        btnFilterCorrect = findViewById(R.id.btn_filter_correct);
//        btnFilterWrong = findViewById(R.id.btn_filter_wrong);
//        rvAnswers = findViewById(R.id.rv_answers);
//
//        tvTitle.setText(title);
//    }
//
//    private void setupRecyclerView() {
//        filteredAnswers = new ArrayList<>(allAnswers);
//        adapter = new AnswerReviewAdapter(this, filteredAnswers);
//        rvAnswers.setLayoutManager(new LinearLayoutManager(this));
//        rvAnswers.setAdapter(adapter);
//    }
//
//    private void displaySummary() {
//        tvCorrectCount.setText(String.valueOf(correctCount));
//        tvWrongCount.setText(String.valueOf(wrongCount));
//        tvSkippedCount.setText(String.valueOf(skippedCount));
//
//        // Hide skipped section if no skipped questions
//        if (skippedCount == 0) {
//            layoutSkipped.setVisibility(View.GONE);
//        }
//    }
//
//    private void setClickListeners() {
//        btnBack.setOnClickListener(v -> finish());
//
//        btnFilterAll.setOnClickListener(v -> applyFilter("ALL"));
//        btnFilterCorrect.setOnClickListener(v -> applyFilter("CORRECT"));
//        btnFilterWrong.setOnClickListener(v -> applyFilter("WRONG"));
//    }
//
//    private void applyFilter(String filter) {
//        currentFilter = filter;
//        filteredAnswers.clear();
//
//        switch (filter) {
//            case "ALL":
//                filteredAnswers.addAll(allAnswers);
//                updateFilterButtons(btnFilterAll);
//                break;
//            case "CORRECT":
//                for (AnswerReview answer : allAnswers) {
//                    if (answer.isCorrect()) {
//                        filteredAnswers.add(answer);
//                    }
//                }
//                updateFilterButtons(btnFilterCorrect);
//                break;
//            case "WRONG":
//                for (AnswerReview answer : allAnswers) {
//                    if (!answer.isCorrect()) {
//                        filteredAnswers.add(answer);
//                    }
//                }
//                updateFilterButtons(btnFilterWrong);
//                break;
//        }
//
//        adapter.notifyDataSetChanged();
//    }
//
//    private void updateFilterButtons(Button selectedButton) {
//        // Reset all buttons
//        btnFilterAll.setBackgroundResource(R.drawable.btn_outline_blue);
//        btnFilterAll.setTextColor(getResources().getColor(R.color.primary_blue));
//        btnFilterCorrect.setBackgroundResource(R.drawable.btn_outline_blue);
//        btnFilterCorrect.setTextColor(getResources().getColor(R.color.primary_blue));
//        btnFilterWrong.setBackgroundResource(R.drawable.btn_outline_blue);
//        btnFilterWrong.setTextColor(getResources().getColor(R.color.primary_blue));
//
//        // Highlight selected button
//        selectedButton.setBackgroundResource(R.drawable.btn_primary);
//        selectedButton.setTextColor(getResources().getColor(R.color.white));
//    }
//}
//
