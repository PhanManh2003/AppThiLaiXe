package com.example.appthilaixe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appthilaixe.R;
import com.example.appthilaixe.models.AnswerReview;

import java.util.List;

public class AnswerReviewAdapter extends RecyclerView.Adapter<AnswerReviewAdapter.ViewHolder> {

    private Context context;
    private List<AnswerReview> answers;

    public AnswerReviewAdapter(Context context, List<AnswerReview> answers) {
        this.context = context;
        this.answers = answers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_answer_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnswerReview answer = answers.get(position);

        // Số câu
        holder.tvQuestionNumber.setText(String.valueOf(position + 1));

        // Trạng thái Đúng/Sai/Bỏ qua
        if (answer.isAnswered()) {
            if (answer.isCorrect()) {
                holder.tvStatusBadge.setText("Đúng");
                holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_success);
            } else {
                holder.tvStatusBadge.setText("Sai");
                holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_failed);
            }
        } else {
            holder.tvStatusBadge.setText("Bỏ qua");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_answer_skipped);
        }

        // Nội dung câu hỏi
        holder.tvQuestionText.setText(answer.getQuestionText());

        // Hiển thị ảnh nếu có
        if (answer.getImagePath() != null && !answer.getImagePath().isEmpty()) {
            holder.ivQuestionImage.setVisibility(View.VISIBLE);
             Glide.with(context).load(answer.getImagePath()).into(holder.ivQuestionImage);
        } else {
            holder.ivQuestionImage.setVisibility(View.GONE);
        }

        // Các option
        holder.tvOptionA.setText(answer.getOptionA());
        holder.tvOptionB.setText(answer.getOptionB());
        holder.tvOptionC.setText(answer.getOptionC());
        holder.tvOptionD.setText(answer.getOptionD());

        // Reset tất cả icon
        holder.ivCheckA.setVisibility(View.GONE);
        holder.ivCheckB.setVisibility(View.GONE);
        holder.ivCheckC.setVisibility(View.GONE);
        holder.ivCheckD.setVisibility(View.GONE);

        holder.ivWrongA.setVisibility(View.GONE);
        holder.ivWrongB.setVisibility(View.GONE);
        holder.ivWrongC.setVisibility(View.GONE);
        holder.ivWrongD.setVisibility(View.GONE);

        // Đánh dấu đáp án đúng (tick xanh)
        switch (answer.getCorrectAnswer().toUpperCase()) {
            case "A": holder.ivCheckA.setVisibility(View.VISIBLE); break;
            case "B": holder.ivCheckB.setVisibility(View.VISIBLE); break;
            case "C": holder.ivCheckC.setVisibility(View.VISIBLE); break;
            case "D": holder.ivCheckD.setVisibility(View.VISIBLE); break;
        }

        // Nếu người dùng trả lời sai, đánh dấu X đỏ cho đáp án họ chọn
        if (answer.isAnswered() && !answer.isCorrect()) {
            switch (answer.getUserAnswer().toUpperCase()) {
                case "A": holder.ivWrongA.setVisibility(View.VISIBLE); break;
                case "B": holder.ivWrongB.setVisibility(View.VISIBLE); break;
                case "C": holder.ivWrongC.setVisibility(View.VISIBLE); break;
                case "D": holder.ivWrongD.setVisibility(View.VISIBLE); break;
            }
        }

        // Giải thích
        if (answer.getExplanation() != null && !answer.getExplanation().isEmpty()) {
            holder.layoutExplanation.setVisibility(View.VISIBLE);
            holder.tvExplanation.setText(answer.getExplanation());
        } else {
            holder.layoutExplanation.setVisibility(View.GONE);
        }

        // Expand/Collapse
        holder.ivExpand.setOnClickListener(v -> {
            if (holder.layoutExpandable.getVisibility() == View.VISIBLE) {
                holder.layoutExpandable.setVisibility(View.GONE);
                holder.ivExpand.setRotation(0);
            } else {
                holder.layoutExpandable.setVisibility(View.VISIBLE);
                holder.ivExpand.setRotation(180);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionNumber, tvStatusBadge, tvQuestionText;
        TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvExplanation;
        ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;
        ImageView ivWrongA, ivWrongB, ivWrongC, ivWrongD;
        ImageView ivQuestionImage, ivExpand;
        LinearLayout layoutExpandable, layoutExplanation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            tvStatusBadge = itemView.findViewById(R.id.tv_status_badge);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);

            tvOptionA = itemView.findViewById(R.id.tv_option_a);
            tvOptionB = itemView.findViewById(R.id.tv_option_b);
            tvOptionC = itemView.findViewById(R.id.tv_option_c);
            tvOptionD = itemView.findViewById(R.id.tv_option_d);

            ivCheckA = itemView.findViewById(R.id.iv_check_a);
            ivCheckB = itemView.findViewById(R.id.iv_check_b);
            ivCheckC = itemView.findViewById(R.id.iv_check_c);
            ivCheckD = itemView.findViewById(R.id.iv_check_d);

            ivWrongA = itemView.findViewById(R.id.iv_wrong_a);
            ivWrongB = itemView.findViewById(R.id.iv_wrong_b);
            ivWrongC = itemView.findViewById(R.id.iv_wrong_c);
            ivWrongD = itemView.findViewById(R.id.iv_wrong_d);

            ivQuestionImage = itemView.findViewById(R.id.iv_question_image);
            ivExpand = itemView.findViewById(R.id.iv_expand);

            layoutExpandable = itemView.findViewById(R.id.layout_expandable);
            layoutExplanation = itemView.findViewById(R.id.layout_explanation);

            tvExplanation = itemView.findViewById(R.id.tv_explanation);
        }
    }
}
