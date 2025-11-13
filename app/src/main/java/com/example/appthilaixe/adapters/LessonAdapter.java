package com.example.appthilaixe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthilaixe.R;
import com.example.appthilaixe.models.Lesson;
import com.example.appthilaixe.models.Question;
import com.example.appthilaixe.models.Study;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessons;
    private OnLessonClickListener listener;

    // map để lưu dữ liệu tính sẵn
    private Map<Integer, Integer> totalQuestionsMap = new HashMap<>();
    private Map<Integer, Integer> learnedCountMap = new HashMap<>();

    public interface OnLessonClickListener {
        void onStartClick(Lesson lesson);
    }

    public LessonAdapter(
            List<Lesson> lessons,
            List<Question> questions,
            List<Study> studies,
            int currentUserId,
            OnLessonClickListener listener
    ) {
        this.lessons = lessons;
        this.listener = listener;

        // đếm tổng số câu hỏi theo lesson
        if (questions != null) {
            for (Question q : questions) {
                int id = q.lessonId;
                totalQuestionsMap.put(id, totalQuestionsMap.getOrDefault(id, 0) + 1);
            }
        }

        // lấy số đã học của user
        if (studies != null) {
            for (Study s : studies) {
                if (s.userId == currentUserId) {
                    learnedCountMap.put(s.lessonId, s.learnedCount);
                }
            }
        }
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        // tên bài học
        holder.tvName.setText(lesson.lessonName);

        int lessonId = lesson.lessonId;

        // lấy dữ liệu
        int total = totalQuestionsMap.getOrDefault(lessonId, 0);
        int learned = learnedCountMap.getOrDefault(lessonId, 0);

        if (learned > total) learned = total;

        holder.tvTotal.setText(String.valueOf(total));
        holder.tvCompleted.setText(String.valueOf(learned));
        holder.tvProgressText.setText(learned + "/" + total);

        int percent = (total == 0) ? 0 : (learned * 100 / total);
        holder.progressBar.setProgress(percent);

        // click
        holder.btnStart.setOnClickListener(v -> {
            if (listener != null) listener.onStartClick(lesson);
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTotal, tvCompleted, tvProgressText;
        ProgressBar progressBar;
        Button btnStart;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_category_name);
            tvTotal = itemView.findViewById(R.id.tv_total_questions);
            tvCompleted = itemView.findViewById(R.id.tv_completed_questions);
            tvProgressText = itemView.findViewById(R.id.tv_progress_text);
            progressBar = itemView.findViewById(R.id.progress_bar);
            btnStart = itemView.findViewById(R.id.btn_start);
        }
    }
}
