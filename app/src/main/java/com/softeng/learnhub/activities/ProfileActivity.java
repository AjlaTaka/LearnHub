package com.softeng.learnhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.softeng.learnhub.R;
import com.softeng.learnhub.utils.PreferencesManager;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * ProfileActivity — user profile editor.
 *
 * Allows the student to:
 *  - Set their display name
 *  - Set their student ID
 *  - View summary stats (quizzes taken, total score)
 *  - Share their profile via implicit Intent (ACTION_SEND)
 *
 * Architecture role (MVC): Controller.
 */
public class ProfileActivity extends AppCompatActivity {

    private PreferencesManager prefs;
    private EditText etName, etStudentId;

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
        setContentView(R.layout.activity_profile);

        prefs = PreferencesManager.getInstance(this);

        setupToolbar();
        bindViews();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Profile");
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

    private void bindViews() {
        etName      = findViewById(R.id.et_username);
        etStudentId = findViewById(R.id.et_student_id);
        Button btnSave  = findViewById(R.id.btn_save_profile);
        Button btnShare = findViewById(R.id.btn_share_profile);
        TextView tvStats = findViewById(R.id.tv_profile_stats);

        // Load existing data
        etName.setText(prefs.getUsername());
        etStudentId.setText(prefs.getStudentId());

        int quizzes    = prefs.getTotalQuizzesTaken();
        int totalScore = prefs.getTotalScore();
        int completed  = 0;
        for (int i = 0; i < 5; i++) {
            if (prefs.isCourseCompleted("completed_course_" + i)) completed++;
        }

        tvStats.setText(
                "Quizzes taken: " + quizzes + "\n" +
                "Total score: " + totalScore + " / 500\n" +
                "Courses completed: " + completed + " / 5"
        );

        // Save profile
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String id   = etStudentId.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Name cannot be empty");
                return;
            }

            prefs.setUsername(name);
            prefs.setStudentId(id);
            Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
        });

        // Share profile — Implicit Intent (ACTION_SEND)
        btnShare.setOnClickListener(v -> {
            String name = prefs.getUsername();
            int score   = prefs.getTotalScore();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "I'm " + name + " on LearnHub! 🎓\n" +
                    "Total Score: " + score + "/500\n" +
                    "Studying Software Engineering on LearnHub.");
            startActivity(Intent.createChooser(shareIntent, "Share Profile"));
        });
    }
}
