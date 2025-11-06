package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.example.appthilaixe.adapters.CategoryAdapter;
import com.example.appthilaixe.models.Category;

import java.util.ArrayList;
import java.util.List;

public class LearningActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private ImageView btnBack;
    private TextInputEditText etSearch;
    private RecyclerView rvCategories;
    private LinearLayout emptyState;
    private CategoryAdapter adapter;
    private List<Category> allCategories;
    private List<Category> filteredCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning);

        initViews();
        setupRecyclerView();
        loadCategories();
        setClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search);
        rvCategories = findViewById(R.id.rv_categories);
        emptyState = findViewById(R.id.empty_state);
    }

    private void setupRecyclerView() {
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        filteredCategories = new ArrayList<>();
        adapter = new CategoryAdapter(filteredCategories, this);
        rvCategories.setAdapter(adapter);
    }

    private void loadCategories() {
        // TODO: Load from database
        allCategories = new ArrayList<>();

        // Sample data
        allCategories.add(new Category(1, "Biển báo giao thông", "Học về các biển báo giao thông", R.drawable.ic_car, 100, 25, R.color.primary_blue));
        allCategories.add(new Category(2, "Luật giao thông", "Các quy tắc giao thông cơ bản", R.drawable.ic_car, 80, 40, R.color.primary_green));
        allCategories.add(new Category(3, "Kỹ năng lái xe", "Kỹ năng lái xe an toàn", R.drawable.ic_car, 120, 60, R.color.primary_orange));
        allCategories.add(new Category(4, "Xử lý tình huống", "Xử lý các tình huống giao thông", R.drawable.ic_car, 90, 30, R.color.primary_blue));
        allCategories.add(new Category(5, "Bảo dưỡng xe", "Kiến thức bảo dưỡng xe cơ bản", R.drawable.ic_car, 70, 50, R.color.primary_green));

        filteredCategories.addAll(allCategories);
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void setClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCategories(String query) {
        filteredCategories.clear();

        if (query.isEmpty()) {
            filteredCategories.addAll(allCategories);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Category category : allCategories) {
                if (category.getName().toLowerCase().contains(lowerQuery) ||
                    category.getDescription().toLowerCase().contains(lowerQuery)) {
                    filteredCategories.add(category);
                }
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredCategories.isEmpty()) {
            rvCategories.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvCategories.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartClick(Category category) {
        // Navigate to CategoryQuestionsActivity with category information
        Intent intent = new Intent(LearningActivity.this, CategoryQuestionsActivity.class);
        intent.putExtra("category_id", category.getId());
        intent.putExtra("category_name", category.getName());
        startActivity(intent);
    }
}

