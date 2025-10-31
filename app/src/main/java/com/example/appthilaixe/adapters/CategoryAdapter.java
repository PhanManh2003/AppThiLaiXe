package com.example.appthilaixe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.R;
import com.example.appthilaixe.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onStartClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateList(List<Category> newList) {
        this.categories = newList;
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCategoryIcon;
        private TextView tvCategoryName;
        private TextView tvCategoryDesc;
        private TextView tvProgressText;
        private ProgressBar progressBar;
        private TextView tvTotalQuestions;
        private TextView tvCompletedQuestions;
        private Button btnStart;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvCategoryDesc = itemView.findViewById(R.id.tv_category_desc);
            tvProgressText = itemView.findViewById(R.id.tv_progress_text);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tvTotalQuestions = itemView.findViewById(R.id.tv_total_questions);
            tvCompletedQuestions = itemView.findViewById(R.id.tv_completed_questions);
            btnStart = itemView.findViewById(R.id.btn_start);
        }

        public void bind(Category category, OnCategoryClickListener listener) {
            tvCategoryName.setText(category.getName());
            tvCategoryDesc.setText(category.getDescription());
            tvTotalQuestions.setText(String.valueOf(category.getTotalQuestions()));
            tvCompletedQuestions.setText(String.valueOf(category.getCompletedQuestions()));

            // Calculate progress
            int progress = category.getProgressPercentage();
            progressBar.setProgress(progress);
            tvProgressText.setText(category.getCompletedQuestions() + "/" + category.getTotalQuestions());

            // Set icon
            ivCategoryIcon.setImageResource(category.getIconResId());

            // Set click listener
            btnStart.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStartClick(category);
                }
            });
        }
    }
}

