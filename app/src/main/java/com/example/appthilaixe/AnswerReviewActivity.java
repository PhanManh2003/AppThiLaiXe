package com.example.appthilaixe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.adapters.AnswerReviewAdapter;
import com.example.appthilaixe.models.AnswerReview;

import java.util.ArrayList;
import java.util.List;

public class AnswerReviewActivity extends AppCompatActivity {

    // Các view trong layout
    private ImageView btnBack;
    private TextView tvTitle;
    private TextView tvCorrectCount, tvWrongCount, tvSkippedCount;
    private LinearLayout layoutSkipped;
    private Button btnFilterAll, btnFilterCorrect, btnFilterWrong;
    private RecyclerView rvAnswers;

    // Adapter và dữ liệu câu trả lời
    private AnswerReviewAdapter adapter;
    private List<AnswerReview> allAnswers;      // Toàn bộ câu trả lời
    private List<AnswerReview> filteredAnswers; // Danh sách câu trả lời theo filter
    private String currentFilter = "ALL";       // ALL, CORRECT, WRONG

    private String title;        // Tiêu đề Activity
    private int correctCount = 0;
    private int wrongCount = 0;
    private int skippedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout (Android 10+)
        EdgeToEdge.enable(this);

        // Set layout
        setContentView(R.layout.activity_answer_review);

        // Lấy dữ liệu từ Intent (title, list answers)
        getDataFromIntent();

        // Khởi tạo view
        initViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Hiển thị summary số câu đúng, sai, bỏ qua
        displaySummary();

        // Set click listener cho các nút
        setClickListeners();

        // Áp dụng filter mặc định (ALL)
        applyFilter("ALL");
    }

    private void getDataFromIntent() {
        // Lấy tiêu đề
        title = getIntent().getStringExtra("title");
        if (title == null || title.isEmpty()) {
            title = "Kết quả thi";
        }

        // Lấy list AnswerReview từ Intent
        allAnswers = (ArrayList<AnswerReview>) getIntent().getSerializableExtra("answers");
        if (allAnswers == null) {
            allAnswers = new ArrayList<>();
        }

        // Tính tổng số câu đúng, sai, bỏ qua
        for (AnswerReview answer : allAnswers) {
            if (answer.isCorrect()) {
                correctCount++;
            } else if (answer.isAnswered()) {
                wrongCount++;
            } else {
                skippedCount++;
            }
        }
    }

    private void initViews() {
        // Gán view từ layout
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvCorrectCount = findViewById(R.id.tv_correct_count);
        tvWrongCount = findViewById(R.id.tv_wrong_count);
        tvSkippedCount = findViewById(R.id.tv_skipped_count);
        layoutSkipped = findViewById(R.id.layout_skipped);
        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterCorrect = findViewById(R.id.btn_filter_correct);
        btnFilterWrong = findViewById(R.id.btn_filter_wrong);
        rvAnswers = findViewById(R.id.rv_answers);

        // Set title
        tvTitle.setText(title);
    }

    private void setupRecyclerView() {
        // Ban đầu filteredAnswers = tất cả câu trả lời
        filteredAnswers = new ArrayList<>(allAnswers);

        // Khởi tạo adapter và gán cho RecyclerView
        adapter = new AnswerReviewAdapter(this, filteredAnswers);
        rvAnswers.setLayoutManager(new LinearLayoutManager(this));
        rvAnswers.setAdapter(adapter);
    }

    private void displaySummary() {
        // Hiển thị số lượng câu đúng, sai, bỏ qua
        tvCorrectCount.setText(String.valueOf(correctCount));
        tvWrongCount.setText(String.valueOf(wrongCount));
        tvSkippedCount.setText(String.valueOf(skippedCount));

        // Nếu không có câu bỏ qua thì ẩn layout
        if (skippedCount == 0) {
            layoutSkipped.setVisibility(View.GONE);
        }
    }

    private void setClickListeners() {
        // Nút back
        btnBack.setOnClickListener(v -> finish());

        // Nút filter
        btnFilterAll.setOnClickListener(v -> applyFilter("ALL"));
        btnFilterCorrect.setOnClickListener(v -> applyFilter("CORRECT"));
        btnFilterWrong.setOnClickListener(v -> applyFilter("WRONG"));
    }

    private void applyFilter(String filter) {
        // Lưu filter hiện tại
        currentFilter = filter;

        // Xóa danh sách hiện tại
        filteredAnswers.clear();

        // Thêm câu theo filter
        switch (filter) {
            case "ALL":
                filteredAnswers.addAll(allAnswers);
                updateFilterButtons(btnFilterAll);
                break;
            case "CORRECT":
                for (AnswerReview answer : allAnswers) {
                    if (answer.isCorrect()) {
                        filteredAnswers.add(answer);
                    }
                }
                updateFilterButtons(btnFilterCorrect);
                break;
            case "WRONG":
                for (AnswerReview answer : allAnswers) {
                    if (!answer.isCorrect()) {
                        filteredAnswers.add(answer);
                    }
                }
                updateFilterButtons(btnFilterWrong);
                break;
        }

        // Thông báo adapter update
        adapter.notifyDataSetChanged();
    }

    private void updateFilterButtons(Button selectedButton) {
        // Reset tất cả button về trạng thái mặc định
        btnFilterAll.setBackgroundResource(R.drawable.btn_outline_blue);
        btnFilterAll.setTextColor(getResources().getColor(R.color.primary_blue));
        btnFilterCorrect.setBackgroundResource(R.drawable.btn_outline_blue);
        btnFilterCorrect.setTextColor(getResources().getColor(R.color.primary_blue));
        btnFilterWrong.setBackgroundResource(R.drawable.btn_outline_blue);
        btnFilterWrong.setTextColor(getResources().getColor(R.color.primary_blue));

        // Highlight button đang chọn
        selectedButton.setBackgroundResource(R.drawable.btn_primary);
        selectedButton.setTextColor(getResources().getColor(R.color.white));
    }
}
