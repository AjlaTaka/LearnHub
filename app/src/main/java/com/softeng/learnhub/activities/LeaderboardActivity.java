package com.softeng.learnhub.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.models.Course;
import com.softeng.learnhub.utils.DataRepository;
import com.softeng.learnhub.utils.PreferencesManager;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

/**
 * LeaderboardActivity — personal score dashboard for all 5 courses.
 *
 * Reads best scores from SharedPreferences and displays them with progress bars.
 * Shows total score, overall letter grade, and per-course breakdown.
 *
 * Architecture role (MVC): Controller.
 */
public class LeaderboardActivity extends AppCompatActivity {

    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferencesManager.getInstance(this);

        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        prefs = PreferencesManager.getInstance(this);

        setupToolbar();
        loadScores();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Scores");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadScores() {
        List<Course> courses = DataRepository.getInstance().getAllCourses();

        TextView tvTotalScore  = findViewById(R.id.tv_total_score);
        TextView tvOverallGrade = findViewById(R.id.tv_overall_grade);
        TextView tvStudentName = findViewById(R.id.tv_leaderboard_name);
        LinearLayout scoreContainer = findViewById(R.id.score_container);

        tvStudentName.setText(prefs.getUsername());

        int total = prefs.getTotalScore();
        tvTotalScore.setText(total + " / 500");

        // Calculate overall grade from average percentage
        int avgPct = (courses.size() > 0) ? total / courses.size() : 0;
        String grade;
        if (avgPct >= 90) grade = "A";
        else if (avgPct >= 80) grade = "B";
        else if (avgPct >= 70) grade = "C";
        else if (avgPct >= 60) grade = "D";
        else grade = "F";
        tvOverallGrade.setText(grade);

        // Add a score row for each course
        scoreContainer.removeAllViews();
        for (Course course : courses) {
            int best = prefs.getBestScore(course.getScoreKey());
            boolean completed = prefs.isCourseCompleted(course.getCompletionKey());
            addScoreRow(scoreContainer, course.getTitle(), best, completed);
        }
    }

    /**
     * Dynamically inflates a score row view and adds it to the container.
     */
    private void addScoreRow(LinearLayout container, String courseName, int scorePercent, boolean completed) {
        // Inflate the row layout
        android.view.View row = getLayoutInflater().inflate(R.layout.item_score_row, container, false);

        TextView tvName     = row.findViewById(R.id.tv_score_course_name);
        TextView tvScore    = row.findViewById(R.id.tv_score_value);
        TextView tvBadge    = row.findViewById(R.id.tv_completion_badge);
        ProgressBar progress = row.findViewById(R.id.progress_score);

        tvName.setText(courseName);
        tvScore.setText(scorePercent + "%");
        progress.setProgress(scorePercent);

        if (completed) {
            tvBadge.setText("✓ PASSED");
            tvBadge.setTextColor(getColor(R.color.color_correct));
        } else if (scorePercent > 0) {
            tvBadge.setText("In Progress");
            tvBadge.setTextColor(getColor(R.color.color_warning));
        } else {
            tvBadge.setText("Not started");
            tvBadge.setTextColor(getColor(R.color.text_secondary));
        }

        container.addView(row);
    }
}
