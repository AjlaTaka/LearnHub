package com.softeng.learnhub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.utils.PreferencesManager;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * MainActivity — the home dashboard of LearnHub.
 *
 * Displays:
 *  - Welcome greeting with student name
 *  - Overall progress summary
 *  - Quick-action cards: Courses, Leaderboard, Profile
 *
 * Navigation:
 *  - Explicit Intent → CourseListActivity
 *  - Explicit Intent → LeaderboardActivity
 *  - Explicit Intent → ProfileActivity
 *  - Explicit Intent → SettingsActivity
 *  - Implicit Intent → browser (Share / About)
 *
 * Architecture role (MVC): Controller — wires Views to Model (PreferencesManager).
 */
public class MainActivity extends AppCompatActivity {

    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = PreferencesManager.getInstance(this);

        // Apply saved dark mode BEFORE activity starts
        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupWelcomeSection();
        setupCards();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh stats whenever we return from another Activity
        setupWelcomeSection();
    }

    // ── Toolbar ───────────────────────────────────────────────────────────────

    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Explicit Intent → SettingsActivity
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_share) {
            // Implicit Intent → share via any app (ACTION_SEND)
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out LearnHub!");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "I'm studying Software Engineering with LearnHub. " +
                    "My total score: " + prefs.getTotalScore() + "/500!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        if (id == R.id.action_about) {
            // Implicit Intent → open browser to a URL (ACTION_VIEW)
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com"));
            startActivity(webIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ── Welcome Section ───────────────────────────────────────────────────────

    private void setupWelcomeSection() {
        TextView tvGreeting     = findViewById(R.id.tv_greeting);
        TextView tvProgress     = findViewById(R.id.tv_progress_summary);
        TextView tvTotalScore   = findViewById(R.id.tv_total_score);
        TextView tvQuizzesTaken = findViewById(R.id.tv_quizzes_taken);

        String name = prefs.getUsername();
        tvGreeting.setText("Welcome back,\n" + name + "! 👋");

        int totalScore = prefs.getTotalScore();
        int quizzes    = prefs.getTotalQuizzesTaken();

        // Count completed courses
        int completed = 0;
        for (int i = 0; i < 5; i++) {
            if (prefs.isCourseCompleted("completed_course_" + i)) completed++;
        }

        tvProgress.setText(completed + "/5 courses completed");
        tvTotalScore.setText(String.valueOf(totalScore));
        tvQuizzesTaken.setText(String.valueOf(quizzes));
    }

    // ── Cards ─────────────────────────────────────────────────────────────────

    private void setupCards() {
        // Start Learning card → CourseListActivity
        CardView cardCourses = findViewById(R.id.card_courses);
        cardCourses.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Leaderboard card → LeaderboardActivity
        CardView cardLeaderboard = findViewById(R.id.card_leaderboard);
        cardLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Profile card → ProfileActivity
        CardView cardProfile = findViewById(R.id.card_profile);
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Settings card → SettingsActivity
        CardView cardSettings = findViewById(R.id.card_settings);
        cardSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}
