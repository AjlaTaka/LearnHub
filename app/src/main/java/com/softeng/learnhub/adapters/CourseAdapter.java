package com.softeng.learnhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.models.Course;
import com.softeng.learnhub.utils.PreferencesManager;

import java.util.List;

/**
 * CourseAdapter — RecyclerView adapter for displaying course cards.
 *
 * Each card shows:
 *  - Course title and subtitle
 *  - Best score with a progress bar
 *  - Completion badge (✓ PASSED or "Not started")
 *
 * Uses the ViewHolder pattern for efficient recycling.
 * Architecture role (MVC): View layer component.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    /**
     * Callback interface so the Activity handles click events (separation of concerns).
     */
    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    private final List<Course> courses;
    private final PreferencesManager prefs;
    private final OnCourseClickListener listener;

    public CourseAdapter(List<Course> courses, PreferencesManager prefs,
                         OnCourseClickListener listener) {
        this.courses  = courses;
        this.prefs    = prefs;
        this.listener = listener;
    }

    // ── RecyclerView.Adapter overrides ────────────────────────────────────────

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course, prefs, listener);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        private final CardView   cardRoot;
        private final TextView   tvTitle;
        private final TextView   tvSubtitle;
        private final TextView   tvScore;
        private final TextView   tvBadge;
        private final TextView   tvCourseNumber;
        private final ProgressBar progressScore;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoot      = itemView.findViewById(R.id.card_course_root);
            tvTitle       = itemView.findViewById(R.id.tv_card_course_title);
            tvSubtitle    = itemView.findViewById(R.id.tv_card_course_subtitle);
            tvScore       = itemView.findViewById(R.id.tv_card_score);
            tvBadge       = itemView.findViewById(R.id.tv_card_badge);
            tvCourseNumber = itemView.findViewById(R.id.tv_course_number);
            progressScore = itemView.findViewById(R.id.progress_card_score);
        }

        void bind(Course course, PreferencesManager prefs, OnCourseClickListener listener) {
            Context ctx = itemView.getContext();

            tvTitle.setText(course.getTitle());
            tvSubtitle.setText(course.getSubtitle());
            tvCourseNumber.setText("COURSE " + (course.getId() + 1));

            int best = prefs.getBestScore(course.getScoreKey());
            progressScore.setProgress(best);
            tvScore.setText(best > 0 ? "Best: " + best + "%" : "Not started");

            boolean completed = prefs.isCourseCompleted(course.getCompletionKey());
            if (completed) {
                tvBadge.setText("✓ PASSED");
                tvBadge.setTextColor(ctx.getColor(R.color.color_correct));
                tvBadge.setVisibility(View.VISIBLE);
            } else if (best > 0) {
                tvBadge.setText("In Progress");
                tvBadge.setTextColor(ctx.getColor(R.color.color_warning));
                tvBadge.setVisibility(View.VISIBLE);
            } else {
                tvBadge.setVisibility(View.INVISIBLE);
            }

            // Click listener delegates to Activity
            cardRoot.setOnClickListener(v -> listener.onCourseClick(course));
        }
    }
}
