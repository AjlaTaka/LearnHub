package com.softeng.learnhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.models.QuizResult;
import com.softeng.learnhub.utils.PreferencesManager;
import com.softeng.learnhub.utils.SoundManager;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesManager prefs = PreferencesManager.getInstance(this);

        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        QuizResult result = (QuizResult) getIntent().getSerializableExtra(QuizActivity.EXTRA_QUIZ_RESULT);
        boolean isNewBest = getIntent().getBooleanExtra("is_new_best", false);

        if (result == null) {
            finish();
            return;
        }

        // Play victory sound when results screen opens
        SoundManager soundManager = SoundManager.getInstance(prefs);
        soundManager.playVictory();

        setupToolbar();
        displayResult(result, isNewBest);
        setupButtons(result);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quiz Results");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void displayResult(QuizResult result, boolean isNewBest) {
        TextView tvCourseName   = findViewById(R.id.tv_result_course_name);
        TextView tvScorePercent = findViewById(R.id.tv_score_percent);
        TextView tvGrade        = findViewById(R.id.tv_letter_grade);
        TextView tvCorrect      = findViewById(R.id.tv_correct_count);
        TextView tvTime         = findViewById(R.id.tv_time_taken);
        TextView tvFeedback     = findViewById(R.id.tv_feedback_message);
        CardView cardNewBest    = findViewById(R.id.card_new_best);
        CardView cardScore      = findViewById(R.id.card_score);

        tvCourseName.setText(result.getCourseName());
        tvScorePercent.setText(result.getScorePercent() + "%");
        tvGrade.setText(result.getLetterGrade());
        tvCorrect.setText(result.getCorrectAnswers() + " / " + result.getTotalQuestions() + " correct");
        tvTime.setText("Time: " + result.getFormattedTime());
        tvFeedback.setText(result.getFeedbackMessage());

        int gradeColor;
        int pct = result.getScorePercent();
        if (pct >= 80)      gradeColor = R.color.color_correct;
        else if (pct >= 60) gradeColor = R.color.color_warning;
        else                gradeColor = R.color.color_wrong;
        tvGrade.setTextColor(getColor(gradeColor));

        if (isNewBest) {
            cardNewBest.setVisibility(View.VISIBLE);
            cardNewBest.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));
        } else {
            cardNewBest.setVisibility(View.GONE);
        }

        cardScore.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
    }

    private void setupButtons(QuizResult result) {
        Button btnRetry   = findViewById(R.id.btn_retry_quiz);
        Button btnCourses = findViewById(R.id.btn_back_to_courses);
        Button btnHome    = findViewById(R.id.btn_go_home);

        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(CourseListActivity.EXTRA_COURSE_ID, result.getCourseId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnCourses.setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}