package com.example.appthilaixe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswerReviewAdapter extends RecyclerView.Adapter<AnswerReviewAdapter.ViewHolder> {

    private final Context context;
    private final List<AnswerReview> answers;

    public AnswerReviewAdapter(Context context, List<AnswerReview> answers) {
        this.context = context;
        this.answers = answers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_answer_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnswerReview item = answers.get(position);

        // Số câu
        holder.tvQuestionNumber.setText(String.valueOf(position + 1));

        // Nội dung câu hỏi
        holder.tvQuestionText.setText(item.getQuestionText());

        // Badge trạng thái: Đúng / Sai / Bỏ qua
        if (!item.isAnswered()) {
            holder.tvStatusBadge.setText("Bỏ qua");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_explanation);
        } else if (item.isCorrect()) {
            holder.tvStatusBadge.setText("Đúng");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_success);
        } else {
            holder.tvStatusBadge.setText("Sai");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_failed);
        }

        // Giải thích: đáp án chọn + đáp án đúng
        String selected = item.isAnswered()
                ? item.getSelectedAnswer()
                : "(chưa trả lời)";
        String explanation = "Đáp án đã chọn: " + selected +
                "\nĐáp án đúng: " + item.getCorrectAnswer();
        holder.tvExplanation.setText(explanation);

        // Mặc định đóng phần chi tiết
        holder.layoutExpandable.setVisibility(View.GONE);
        holder.ivExpand.setRotation(0f);

        // Toggle khi bấm vào icon mũi tên
        holder.ivExpand.setOnClickListener(v -> {
            if (holder.layoutExpandable.getVisibility() == View.VISIBLE) {
                holder.layoutExpandable.setVisibility(View.GONE);
                holder.ivExpand.setRotation(0f);
            } else {
                holder.layoutExpandable.setVisibility(View.VISIBLE);
                holder.ivExpand.setRotation(180f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answers != null ? answers.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionNumber;
        TextView tvStatusBadge;
        TextView tvQuestionText;
        TextView tvExplanation;
        ImageView ivExpand;
        LinearLayout layoutExpandable;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            tvStatusBadge = itemView.findViewById(R.id.tv_status_badge);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            tvExplanation = itemView.findViewById(R.id.tv_explanation);
            ivExpand = itemView.findViewById(R.id.iv_expand);
            layoutExpandable = itemView.findViewById(R.id.layout_expandable);
        }
    }
}
