package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.adapters.CategoryAdapter;
import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.models.Category;
import com.example.appthilaixe.repositories.CategoryRepository;
import com.google.android.material.textfield.TextInputEditText;

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

    private int userId = 1;

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

        CategoryRepository repo = new CategoryRepository(this);
        allCategories = repo.loadWithProgress(userId);

        filteredCategories.clear();
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
            for (Category c : allCategories) {
                if (c.getName().toLowerCase().contains(lowerQuery) ||
                        c.getDescription().toLowerCase().contains(lowerQuery)) {
                    filteredCategories.add(c);
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
        Intent intent = new Intent(this, CategoryQuestionsActivity.class);
        intent.putExtra("category_id", category.getId());
        intent.putExtra("category_name", category.getName());
        startActivity(intent);
    }
}
