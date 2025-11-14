package com.example.appthilaixe.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.R;

import java.util.List;

public class ExamHistoryAdapter extends RecyclerView.Adapter<ExamHistoryAdapter.ExamHistoryViewHolder> {

    // Model hiển thị cho từng dòng trong lịch sử
    public static class ExamHistoryItem {
        private final int testId;
        private final String testName;
        private final double resultPercentage;
        private final boolean passed;

        public ExamHistoryItem(int testId, String testName, double resultPercentage, boolean passed) {
            this.testId = testId;
            this.testName = testName;
            this.resultPercentage = resultPercentage;
            this.passed = passed;
        }

        public int getTestId() {
            return testId;
        }

        public String getTestName() {
            return testName;
        }

        public double getResultPercentage() {
            return resultPercentage;
        }

        public boolean isPassed() {
            return passed;
        }
    }

    private final List<ExamHistoryItem> items;

    public ExamHistoryAdapter(List<ExamHistoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ExamHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam_history, parent, false);
        return new ExamHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHistoryViewHolder holder, int position) {
        ExamHistoryItem item = items.get(position);

        holder.tvTestName.setText(item.getTestName());
        holder.tvScore.setText("Kết quả: " + (int) item.getResultPercentage() + "%");
        holder.tvSubInfo.setText("Đề #" + item.getTestId());

        if (item.isPassed()) {
            holder.tvStatus.setText("Đạt");
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // xanh
        } else {
            holder.tvStatus.setText("Trượt");
            holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // đỏ
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ExamHistoryViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTestName;
        final TextView tvScore;
        final TextView tvStatus;
        final TextView tvSubInfo;

        public ExamHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTestName = itemView.findViewById(R.id.tv_test_name);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvSubInfo = itemView.findViewById(R.id.tv_sub_info);
        }
    }
}
