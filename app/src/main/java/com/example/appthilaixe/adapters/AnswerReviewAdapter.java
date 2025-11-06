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

import com.example.appthilaixe.R;
import com.example.appthilaixe.models.AnswerReview;
import com.example.appthilaixe.models.Question;

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
        Question question = answer.getQuestion();

        // Set question number
        holder.tvQuestionNumber.setText(String.valueOf(position + 1));

        // Set status badge
        if (answer.isCorrect()) {
            holder.tvStatusBadge.setText("Đúng");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_success);
        } else if (answer.isAnswered()) {
            holder.tvStatusBadge.setText("Sai");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_error);
        } else {
            holder.tvStatusBadge.setText("Bỏ qua");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_warning);
        }

        // Set question text
        holder.tvQuestionText.setText(question.getQuestionText());

        // Show/hide question image
        if (question.hasImage()) {
            holder.ivQuestionImage.setImageResource(question.getImageResId());
            holder.ivQuestionImage.setVisibility(View.VISIBLE);
        } else {
            holder.ivQuestionImage.setVisibility(View.GONE);
        }

        // Set options
        holder.tvOptionA.setText(question.getOptionA());
        holder.tvOptionB.setText(question.getOptionB());
        holder.tvOptionC.setText(question.getOptionC());
        holder.tvOptionD.setText(question.getOptionD());

        // Highlight correct and user's answer
        highlightAnswers(holder, answer);

        // Set explanation
        holder.tvExplanation.setText(question.getExplanation());

        // Toggle expandable content
        holder.itemView.setOnClickListener(v -> {
            if (holder.layoutExpandable.getVisibility() == View.VISIBLE) {
                holder.layoutExpandable.setVisibility(View.GONE);
                holder.ivExpand.setRotation(0);
            } else {
                holder.layoutExpandable.setVisibility(View.VISIBLE);
                holder.ivExpand.setRotation(180);
            }
        });
    }

    private void highlightAnswers(ViewHolder holder, AnswerReview answer) {
        String correctAnswer = answer.getCorrectAnswer();
        String userAnswer = answer.getUserAnswer();

        // Reset all options
        resetOption(holder.layoutOptionA, holder.ivCheckA);
        resetOption(holder.layoutOptionB, holder.ivCheckB);
        resetOption(holder.layoutOptionC, holder.ivCheckC);
        resetOption(holder.layoutOptionD, holder.ivCheckD);

        // Highlight correct answer
        highlightCorrectAnswer(holder, correctAnswer);

        // Highlight user's wrong answer if different from correct
        if (userAnswer != null && !userAnswer.equals(correctAnswer)) {
            highlightWrongAnswer(holder, userAnswer);
        }
    }

    private void resetOption(LinearLayout layout, ImageView check) {
        layout.setBackgroundResource(R.drawable.answer_option_bg);
        check.setVisibility(View.GONE);
    }

    private void highlightCorrectAnswer(ViewHolder holder, String answer) {
        switch (answer) {
            case "A":
                holder.layoutOptionA.setBackgroundResource(R.drawable.answer_option_correct);
                holder.ivCheckA.setVisibility(View.VISIBLE);
                holder.ivCheckA.setImageResource(R.drawable.ic_check);
                break;
            case "B":
                holder.layoutOptionB.setBackgroundResource(R.drawable.answer_option_correct);
                holder.ivCheckB.setVisibility(View.VISIBLE);
                holder.ivCheckB.setImageResource(R.drawable.ic_check);
                break;
            case "C":
                holder.layoutOptionC.setBackgroundResource(R.drawable.answer_option_correct);
                holder.ivCheckC.setVisibility(View.VISIBLE);
                holder.ivCheckC.setImageResource(R.drawable.ic_check);
                break;
            case "D":
                holder.layoutOptionD.setBackgroundResource(R.drawable.answer_option_correct);
                holder.ivCheckD.setVisibility(View.VISIBLE);
                holder.ivCheckD.setImageResource(R.drawable.ic_check);
                break;
        }
    }

    private void highlightWrongAnswer(ViewHolder holder, String answer) {
        switch (answer) {
            case "A":
                holder.layoutOptionA.setBackgroundResource(R.drawable.answer_option_wrong);
                holder.ivCheckA.setVisibility(View.VISIBLE);
                holder.ivCheckA.setImageResource(R.drawable.ic_close);
                break;
            case "B":
                holder.layoutOptionB.setBackgroundResource(R.drawable.answer_option_wrong);
                holder.ivCheckB.setVisibility(View.VISIBLE);
                holder.ivCheckB.setImageResource(R.drawable.ic_close);
                break;
            case "C":
                holder.layoutOptionC.setBackgroundResource(R.drawable.answer_option_wrong);
                holder.ivCheckC.setVisibility(View.VISIBLE);
                holder.ivCheckC.setImageResource(R.drawable.ic_close);
                break;
            case "D":
                holder.layoutOptionD.setBackgroundResource(R.drawable.answer_option_wrong);
                holder.ivCheckD.setVisibility(View.VISIBLE);
                holder.ivCheckD.setImageResource(R.drawable.ic_close);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvStatusBadge, tvQuestionText;
        ImageView ivQuestionImage, ivExpand;
        LinearLayout layoutExpandable;
        TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
        LinearLayout layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD;
        ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;
        TextView tvExplanation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            tvStatusBadge = itemView.findViewById(R.id.tv_status_badge);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            ivQuestionImage = itemView.findViewById(R.id.iv_question_image);
            ivExpand = itemView.findViewById(R.id.iv_expand);
            layoutExpandable = itemView.findViewById(R.id.layout_expandable);
            tvOptionA = itemView.findViewById(R.id.tv_option_a);
            tvOptionB = itemView.findViewById(R.id.tv_option_b);
            tvOptionC = itemView.findViewById(R.id.tv_option_c);
            tvOptionD = itemView.findViewById(R.id.tv_option_d);
            layoutOptionA = itemView.findViewById(R.id.layout_option_a);
            layoutOptionB = itemView.findViewById(R.id.layout_option_b);
            layoutOptionC = itemView.findViewById(R.id.layout_option_c);
            layoutOptionD = itemView.findViewById(R.id.layout_option_d);
            ivCheckA = itemView.findViewById(R.id.iv_check_a);
            ivCheckB = itemView.findViewById(R.id.iv_check_b);
            ivCheckC = itemView.findViewById(R.id.iv_check_c);
            ivCheckD = itemView.findViewById(R.id.iv_check_d);
            tvExplanation = itemView.findViewById(R.id.tv_explanation);
        }
    }
}

